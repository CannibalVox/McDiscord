package net.technicpack.mcdiscord.discord.api.http;

import com.google.gson.Gson;
import cpw.mods.fml.common.FMLLog;
import net.technicpack.mcdiscord.discord.callback.DiscordCallback;
import net.technicpack.mcdiscord.discord.api.IAuthedDiscordApi;
import net.technicpack.mcdiscord.discord.callback.DiscordResponseHandler;
import net.technicpack.mcdiscord.discord.io.auth.AuthRequest;
import net.technicpack.mcdiscord.discord.io.auth.AuthResponse;
import net.technicpack.mcdiscord.discord.io.server.Server;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * This secretly has both authed & unauthed methods on it :O
 * In order to prevent confusion, we package protect the constructors to pressure people
 * into using the factory (and therefore interacting with the interfaces)
 */
public class HttpDiscordApi implements IAuthedDiscordApi {

    private String url;
    private DiscordResponseHandler responseHandler;

    private boolean isAuthed;
    private String authToken;

    HttpDiscordApi(String url, DiscordResponseHandler responseHandler) {
        this.url = url;
        this.isAuthed = false;
        this.responseHandler = responseHandler;
    }

    private HttpDiscordApi(String url, DiscordResponseHandler responseHandler, String token) {
        this.url = url;
        this.authToken = token;
        this.isAuthed = true;
        this.responseHandler = responseHandler;
    }

    @Override
    public void getServer(final String serverId, final DiscordCallback<Server> callback) {
        if (serverId == null || serverId.isEmpty())
            return;

        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronousGetServer(serverId, callback);
            }
        }).start();
    }

    @Override
    public void authenticate(final AuthRequest request, final DiscordCallback<IAuthedDiscordApi> callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronousAuthenticate(request, callback);
            }
        }).start();
    }

    protected void synchronousGetServer(String serverId, DiscordCallback<Server> callback) {
        String serverUrl = this.url + "servers/" + serverId + "/widget.json";

        Server server = null;

        try {
            server = getRestObject(Server.class, serverUrl);
        } catch (IOException ex) {
            FMLLog.getLogger().error("Error pulling server from Discord.", ex);
        }

        postCallback(server, callback);
    }

    protected void synchronousAuthenticate(AuthRequest request, DiscordCallback<IAuthedDiscordApi> callback) {
        if (this.isAuthed) {
            postCallback(this, callback);
            return;
        }

        AuthResponse response = null;
        try {
            String authUrl = this.url + "auth/login";
            response = postRestObject(AuthResponse.class, request, authUrl);
        } catch (IOException ex) {
            FMLLog.getLogger().error("Error authenticating with Discord.", ex);
        }

        if (response == null) {
            FMLLog.severe("Discord returned a null auth response with no error message.");
        } else {
            for (String error : response.getUsernameErrors()) {
                FMLLog.severe("Discord had a problem with the username: %s",error);
            }
            for (String error : response.getPasswordErrors()) {
                FMLLog.severe("Discord has a problem with the password: %s",error);
            }

            if (response.getPasswordErrors().size() == 0 && response.getUsernameErrors().size() == 0) {
                if (response.getToken() == null || response.getToken().isEmpty()) {
                    FMLLog.severe("Discord returned an auth response with no errors, but also no auth token.");
                }

                postCallback(new HttpDiscordApi(this.url, this.responseHandler, response.getToken()), callback);
                return;
            }
        }

        postCallback(null, callback);
    }

    protected <T> void postCallback(T callbackData, DiscordCallback<T> callback) {
        callback.setValue(callbackData);
        responseHandler.add(callback);
    }

    private final static Gson gson = new Gson();
    protected static <T> T postRestObject(Class<T> restObject, Object postData, String url) throws IOException {

        InputStream stream = null;
        String postObj = gson.toJson(postData);
        try {
            URLConnection conn = new URL(url).openConnection();
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(15000);

            OutputStream outputStream = null;
            try {
                outputStream = conn.getOutputStream();
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"), true);
                writer.write(postObj);
                writer.flush();
                outputStream.close();
            } finally {
                if (outputStream != null)
                    IOUtils.closeQuietly(outputStream);
            }

            stream = conn.getInputStream();
            String data = IOUtils.toString(stream, Charsets.UTF_8);
            T result = gson.fromJson(data, restObject);

            return result;
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }

    public static <T extends Object> T getRestObject(Class<T> restObject, String url) throws IOException {
        return getRestObject(restObject, url, null);
    }

    public static <T extends Object> T getRestObject(Class<T> restObject, String url, String token) throws IOException {
        InputStream stream = null;
        try {
            URLConnection conn = new URL(url).openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");

            if (token != null)
                conn.setRequestProperty("authorization", token);

            conn.setConnectTimeout(15000);
            conn.setReadTimeout(15000);

            stream = conn.getInputStream();
            String data = IOUtils.toString(stream, Charsets.UTF_8);
            T result = gson.fromJson(data, restObject);

            return result;
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }
}
