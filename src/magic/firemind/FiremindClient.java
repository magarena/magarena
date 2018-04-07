package magic.firemind;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import magic.data.CardDefinitions;
import magic.data.GeneralConfig;
import magic.utility.MagicFileSystem;
import magic.utility.MagicFileSystem.DataPath;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FiremindClient {

    protected static final GeneralConfig CONFIG = GeneralConfig.getInstance();
    static String firemindHost ;
    static List<String> addedScripts;
    public static Duel popDeckJob() {
        CONFIG.load();
        JSONObject obj;
        try {
            obj = readJsonFromUrl(firemindHost + "/api/v1/duel_jobs");

            Duel d = new Duel();
            d.id = obj.getInt("id");
            d.games_to_play = obj.getInt("games_to_play");
            d.deck1_text = obj.getString("deck1_text");
            d.deck2_text = obj.getString("deck2_text");
            d.seed = obj.getInt("seed");
            d.strAi1 = obj.getInt("str_ai1");
            d.strAi2 = obj.getInt("str_ai2");
            d.life = obj.getInt("life");
            d.ai1 = obj.getString("ai1");
            d.ai2 = obj.getString("ai2");

            JSONArray scripts = obj.getJSONArray("card_scripts");
            addedScripts = new ArrayList<>();
            if(scripts != null){
                for (int i = 0; i < scripts.length(); i++) {
                    JSONObject script = scripts.getJSONObject(i);
                    String name = script.getString("name");

                    saveScriptFile(name, "txt", script.getString("config"));
                    String groovyScript = script.getString("script");
                    if(groovyScript != null && !groovyScript.equals("")){
                        saveScriptFile(name, "groovy", groovyScript);
                    }

                    System.out.println(name);
                }
            }

            return d;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (FileNotFoundException e) {
            // No need to handle the 404 error
            return null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    private static void saveScriptFile(String name, String extension, String content){
        MagicFileSystem.getDataPath(DataPath.SCRIPTS_ORIG).toFile().mkdirs();
        File scriptsDirectory = MagicFileSystem.getDataPath(DataPath.SCRIPTS).toFile();
        File scriptOrigsDirectory = MagicFileSystem.getDataPath(DataPath.SCRIPTS_ORIG).toFile();
        String filename = CardDefinitions.getCanonicalName(name)+"."+extension;
        File f = new File(scriptsDirectory.getAbsolutePath()+"/"+filename);
        if (f.exists()){
            f.renameTo(new File(scriptOrigsDirectory.getAbsolutePath()+"/"+filename+".orig"));
        }else{
            addedScripts.add(f.getAbsolutePath());
        }
        try {
            f.createNewFile();
        }catch (IOException e){
            System.err.println("Couldn't save script file");
        }
        try{
            PrintWriter writer = new PrintWriter(f.getAbsolutePath(), "UTF-8");
            writer.println(content);
            writer.close();
            System.out.println(f.getAbsolutePath());
        }catch (FileNotFoundException | UnsupportedEncodingException e){
            System.err.println("Couldn't save script file");
        }
    }

    public static void resetChangedScripts(){
        File scriptsDirectory = MagicFileSystem.getDataPath(DataPath.SCRIPTS_ORIG).toFile();
        MagicFileSystem.getDataPath(DataPath.SCRIPTS_ORIG).toFile().mkdirs();
        String[] ext = new String[]{"orig"};
        List<File> files = (List<File>) FileUtils.listFiles(MagicFileSystem.getDataPath(DataPath.SCRIPTS_ORIG).toFile(), ext, false);
        for(File f: files){
            f.renameTo(new File(scriptsDirectory.getAbsolutePath()+"/"+f.getName().substring(0, f.getName().lastIndexOf("."))));
        }
        for(String path: addedScripts){
            (new File(path)).delete();
        }
    }

    public static boolean postGame(
        Integer duel_id,
        Integer game_number,
        Date play_time,
        boolean win_deck1,
        Integer magarena_version_major,
        Integer magarena_version_minor,
        String logFile
    ) {
        CONFIG.load();
        String url = firemindHost + "/api/v1/duel_jobs/" + duel_id + "/games";
        System.out.println("Posting game result "+game_number);
        URL object;
        try {
            object = new URL(url);
            HttpURLConnection con = (HttpURLConnection) object.openConnection();
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Authorization", "Token token="
                    + CONFIG.getFiremindAccessToken());
            con.setRequestMethod("POST");

            JSONObject parent = new JSONObject();
            parent.put("game_number", game_number);
            SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
            df.setTimeZone(TimeZone.getTimeZone("GMT"));
            parent.put("play_time", df.format(play_time) + " GMT");
            parent.put("win_deck1", win_deck1);
            parent.put("magarena_version_major", magarena_version_major);
            parent.put("magarena_version_minor", magarena_version_minor);
            parent.put("log", new String(Files.readAllBytes(Paths.get(logFile)), UTF_8));
            con.setDoOutput(true);

            con.setDoInput(true);
            OutputStreamWriter wr = new OutputStreamWriter(
                    con.getOutputStream(), UTF_8);
            wr.write(parent.toString());
            wr.flush();
            int HttpResult = con.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                return true;
            } else {
                System.err.println(con.getResponseMessage());
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public static boolean postFailure(Integer duel_id, String text) {
        CONFIG.load();
        String url = firemindHost + "/api/v1/duel_jobs/" + duel_id + "/post_failure";
        URL object;
        try {
            object = new URL(url);
            HttpURLConnection con = (HttpURLConnection) object.openConnection();
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Authorization", "Token token="
                    + CONFIG.getFiremindAccessToken());
            con.setRequestMethod("POST");

            JSONObject parent = new JSONObject();
            parent.put("failure_message", text);
            con.setDoOutput(true);

            con.setDoInput(true);
            OutputStreamWriter wr = new OutputStreamWriter(
                    con.getOutputStream(), UTF_8);
            wr.write(parent.toString());
            wr.flush();
            int HttpResult = con.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                return true;
            } else {
                System.err.println(con.getResponseMessage());
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }

    public static boolean postSuccess(Integer duel_id) {
        CONFIG.load();
        String url = firemindHost + "/api/v1/duel_jobs/" + duel_id + "/post_success";
        URL object;
        try {
            object = new URL(url);
            HttpURLConnection con = (HttpURLConnection) object.openConnection();
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Authorization", "Token token="
                    + CONFIG.getFiremindAccessToken());
            con.setRequestMethod("POST");

            con.setDoOutput(true);

            int HttpResult = con.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                return true;
            } else {
                System.err.println(con.getResponseMessage());
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException,
            JSONException {
        HttpURLConnection con = (HttpURLConnection) (new URL(url))
                .openConnection();
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("Authorization", "Token token=" + CONFIG.getFiremindAccessToken());
        con.setRequestMethod("DELETE");
        con.setDoOutput(true);

        con.setDoInput(true);

        BufferedReader rd = new BufferedReader(new InputStreamReader(
                con.getInputStream(), Charset.forName("UTF-8")));
        String jsonText = readAll(rd);
        JSONObject json = new JSONObject(jsonText);
        return json;
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static void setFiremindHost(String host) {
        firemindHost = host;
    }


    public static boolean checkMagarenaVersion(String magarenaVersion) {
        String url = firemindHost + "/api/v1/status/client_info";
        HttpURLConnection con;
        try {
            con = (HttpURLConnection) (new URL(url)).openConnection();

            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Authorization", "Token token=" + CONFIG.getFiremindAccessToken());
            con.setDoOutput(true);

            con.setDoInput(true);

            BufferedReader rd;
            rd = new BufferedReader(new InputStreamReader(
            con.getInputStream(), Charset.forName("UTF-8")));
            String jsonText = readAll(rd);

            JSONObject json = new JSONObject(jsonText);
            String[] remoteVersion = json.getString("current_magarena_version").split("\\.");
            String[] localVersion = magarenaVersion.split("\\.");
            return Integer.valueOf(localVersion[0]) >= Integer.valueOf(remoteVersion[0]) && Integer.valueOf(localVersion[1]) >= Integer.valueOf(remoteVersion[1]);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static void setHostByEnvironment(){
        String env;
        try {
            if (java.net.InetAddress.getLocalHost().getHostName().equals("mike-AndroidDev")) {
                env = "development";
            } else {
                env = "production";
            }
        } catch (UnknownHostException e1) {
            env = "production";
        }
        if (env.equals("production")) {
            FiremindClient.setFiremindHost("https://www.firemind.ch");
        } else {
            FiremindClient.setFiremindHost("http://192.168.50.10");
        }
    }
}
