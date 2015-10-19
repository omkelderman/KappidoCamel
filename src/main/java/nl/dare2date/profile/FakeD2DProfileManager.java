package nl.dare2date.profile;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.*;

/**
 * A fake profile manager, used to similate a database of users and their info. The 'database' consists of a
 * resources/fakeUsers.json.
 */
public class FakeD2DProfileManager implements ID2DProfileManager {
    private static Map<Integer, FakeD2DUser> users;

    public FakeD2DProfileManager() {
        InputStream fakeUsersResource = FakeD2DProfileManager.class.getClassLoader().getResourceAsStream("fakeUsers.json");
        if (fakeUsersResource == null) throw new IllegalStateException("Could not find the fake users JSON file!");
        Gson gson = new Gson();

        users = new HashMap<>();

        //Deserialize the json file into a list of Dare2Date users.
        Type fakeD2DUserListType = new TypeToken<List<FakeD2DUser>>() {
        }.getType();
        ArrayList<FakeD2DUser> userList = gson.fromJson(new InputStreamReader(fakeUsersResource), fakeD2DUserListType);
        for (FakeD2DUser user : userList) {
            users.put(user.getUserId(), user);
        }
    }

    @Override
    public String getTwitchId(int dare2DateUserId) {
        FakeD2DUser user = users.get(dare2DateUserId);
        return user == null ? null : user.getTwitchId();
    }

    @Override
    public String getSteamId(int dare2DateUserId) {
        FakeD2DUser user = users.get(dare2DateUserId);
        return user == null ? null : user.getSteamId();
    }

    @Override
    public Set<Integer> getAllUsers() {
        return users.keySet();
    }
}
