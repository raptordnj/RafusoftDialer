package org.linphone.classes;
import com.google.gson.annotations.SerializedName;

public class BalanceResponse {
    @SerializedName("result")
    private String result;
    @SerializedName("error")
    private String error;
    @SerializedName("username")
    private String username;
    @SerializedName("credit")
    private String credit;

    // Add getters and setters as needed
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }
}
