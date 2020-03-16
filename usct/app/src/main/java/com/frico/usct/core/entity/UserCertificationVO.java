package com.frico.usct.core.entity;

public class UserCertificationVO {

    /**
     * userCertification : {"realname":"暂住证","idnum":"111222333444555666","id_card_obverse":"uploads/certification/20200102/97cd84a192e175dff879ad1bd553261a.jpg","id_card_reverse":"uploads/certification/20200102/3dcacd5487e6da8b9672c96282788fb9.jpg"}
     */

    private UserCertificationBean userCertification;

    public UserCertificationBean getUserCertification() {
        return userCertification;
    }

    public void setUserCertification(UserCertificationBean userCertification) {
        this.userCertification = userCertification;
    }

    public static class UserCertificationBean {
        /**
         * realname : 暂住证
         * idnum : 111222333444555666
         * id_card_obverse : uploads/certification/20200102/97cd84a192e175dff879ad1bd553261a.jpg
         * id_card_reverse : uploads/certification/20200102/3dcacd5487e6da8b9672c96282788fb9.jpg
         */

        private String realname;
        private String idnum;
        private String id_card_obverse;
        private String id_card_reverse;

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public String getIdnum() {
            return idnum;
        }

        public void setIdnum(String idnum) {
            this.idnum = idnum;
        }

        public String getId_card_obverse() {
            return id_card_obverse;
        }

        public void setId_card_obverse(String id_card_obverse) {
            this.id_card_obverse = id_card_obverse;
        }

        public String getId_card_reverse() {
            return id_card_reverse;
        }

        public void setId_card_reverse(String id_card_reverse) {
            this.id_card_reverse = id_card_reverse;
        }
    }
}
