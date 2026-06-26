package model.response;

public class SingleUserResponse {

    private UserData data;

    public UserData getData() {
        return data;
    }

    public static class UserData {

        private Integer id;
        private String email;
        private String first_name;
        private String last_name;
        private String avatar;

        public Integer getId() {
            return id;
        }

        public String getEmail() {
            return email;
        }

        public String getFirst_name() {
            return first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public String getAvatar() {
            return avatar;
        }
    }
}