package JavaFXClientServer.JsonIO;

import org.json.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class JsonDict {
    private JSONObject dict;

    public JsonDict(String filename) {
        dict = load(filename);
    }

//    public static void main(String[] args) {
//        // 从磁盘读取json文件
//        String filename = "/Users/rongxinzhu/IdeaProjects/socket_example/src/JavaFXClientServer.JsonIO/example.json";
//        JSONObject dict = load(filename);
//
//        // 根据单词查询词义
//        String word = "happy";
//        String meaning = searchWordMeaning(dict, word);
//        System.out.println(meaning);
//
//        // 检测词典中是否有某个单词
//        boolean wordExists = dict.has("mouse");
//        System.out.println(wordExists);
//
//        // 添加新单词
//        String key = "keyboard";
//        String value = "a kind of input device of computers";
//        boolean addSuccess = addWord(dict, key, value);
//        System.out.println(addSuccess);
//
//        // 删除已有单词
//        word = "happy";
//        boolean deleteSucess = deleteWord(dict, word);
//        System.out.println(deleteSucess);
//
//        // 将词典保存到磁盘
//        boolean saveSuccess = saveJson(dict, filename);
//        System.out.println(saveSuccess);
//    }

    public JSONObject load(String filename) {
        try {
            JSONTokener tokener = new JSONTokener(new FileReader(filename));
            JSONObject dict = new JSONObject(tokener);
            return dict;
        } catch (JSONException| FileNotFoundException e) {
            System.out.println("Failed to load json file.");
        }
        return null;
    }

    public String searchWordMeaning(String word) {
        try {
            return dict.getString(word);
        } catch (JSONException e) {
            System.out.println("Failed to search word <" + word + ">");
        }
        return null;
    }

    public boolean addWord(String word, String meaning) {
        // TODO: 检测词典中是否已有这个词
        try {
            dict.put(word, meaning);
            return true;
        } catch (JSONException e) {
            System.out.println("Failed to add word <" + word + ">");
        }
        return false;
    }

    public boolean deleteWord(String word) {
        try {
            dict.remove(word);
            return true;
        } catch (Exception e) {
            System.out.println("Failed to delete word <" + word + ">");
        }
        return false;
    }

    public boolean saveJson(String filename) {
        try {
            FileWriter fileWriter = new FileWriter(filename, false);
            fileWriter.write(dict.toString(4));
            fileWriter.flush();
            fileWriter.close();
            return true;
        } catch (IOException|JSONException e) {
            System.out.println("Failded to save json.");
        }
        return false;
    }

    public String toString() {
        try {
            return dict.toString(4);
        } catch (JSONException e) {
            System.out.println("Failed to convert dictionary to string.");
        }
        return null;
    }
}
