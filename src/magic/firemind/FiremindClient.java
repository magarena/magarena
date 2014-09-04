package firemind;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import magic.data.GeneralConfig;

import org.json.JSONException;
import org.json.JSONObject;

public class FiremindClient {

    protected static final GeneralConfig CONFIG = GeneralConfig.getInstance();
	static String firemindHost ;

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

	public static boolean postGame(Integer duel_id, Integer game_number,
			Date play_time, boolean win_deck1, Integer magarena_version_major,
			Integer magarena_version_minor, String logFile) {
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
			parent.put("play_time", play_time);
			parent.put("win_deck1", win_deck1);
			parent.put("magarena_version_major", magarena_version_major);
			parent.put("magarena_version_minor", magarena_version_minor);
			parent.put("log", new String(Files.readAllBytes(Paths.get(logFile))));
			con.setDoOutput(true);

			con.setDoInput(true);
			OutputStreamWriter wr = new OutputStreamWriter(
					con.getOutputStream());
			wr.write(parent.toString());
			wr.flush();
			int HttpResult = con.getResponseCode();
			if (HttpResult == HttpURLConnection.HTTP_OK) {
				return true;
			} else {
				System.err.println(con.getResponseMessage());
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public static boolean postFailure(Integer duel_id, String text) {
		System.err.println("POST ERROR STUB: "+text);
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
}
