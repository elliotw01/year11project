package wood.elliot;

public class GameResult {

    String username;
    Integer score;

    public GameResult(String user, Integer sc) {
        username = user;
        score = sc;
    }

    public Integer getScore() {
        return score;
    }

    @Override
    public String toString() {
        return username + " " + score;
    }
}
