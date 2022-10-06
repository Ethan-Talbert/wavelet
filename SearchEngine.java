import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

class Handler implements URLHandler {
    // The one bit of state on the server: a number that will be manipulated by
    // various requests.
    ArrayList<String> dictionary = new ArrayList<String>();

    public String handleRequest(URI url) {
        if (url.getPath().equals("/")) {
            return String.format("Welcome to the search engine! There are currently %d items stored.", dictionary.size());
        } else if (url.getPath().contains("/add")) {
            String[] parameters = url.getQuery().split("=");
            if (parameters[0].equals("s")) {
                dictionary.add(parameters[1]);
                return String.format("%s added to the dictionary!", parameters[1]);
            }
        } else if (url.getPath().contains("/search")) {
            String[] parameters = url.getQuery().split("=");
            if (parameters[0].equals("s")) {
                ArrayList<String> temp = new ArrayList<String>();
                for (int i = 0; i < dictionary.size(); i ++) {
                    if (dictionary.get(i).contains(parameters[1])) {
                        temp.add(dictionary.get(i));
                    }
                }
                return this.formatArrayList(temp);
            }
        }
        return "404 Not Found!";
    }

    public String formatArrayList(ArrayList array) {
        String str = "Matches: ";
        for (int i = 0; i < array.size(); i ++) {
            str += array.get(i);
            if (i < array.size() - 1) {
                str += ", ";
            }
        }
        return str;
    }
}

class SearchEngine {
    public static void main(String[] args) throws IOException {
        if(args.length == 0){
            System.out.println("Missing port number! Try any number between 1024 to 49151");
            return;
        }

        int port = Integer.parseInt(args[0]);

        Server.start(port, new Handler());
    }
}
