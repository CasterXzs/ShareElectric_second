package com.xzs.shareelectric_second.utils;



public class Config {

    public static final String BASEHOST = "http://snowleaf.xyz:8080/electric";

    public static final String REGISTER = BASEHOST+"/UserAction_register";
    public static final String LOGIN = BASEHOST+"/UserAction_login.action";
    public static final String SCORE = BASEHOST + "/mobile/UserServlet?method=getRelationScore";
    public static final String ADDFD = BASEHOST + "/mobile/UserServlet?method=addFriend";
    public static final String GETFD = BASEHOST + "/mobile/UserServlet?method=listFriend&uid=";
    public static final String UPDATA = BASEHOST + "/UserAction_updateUser.action";

    public static final String UPNEW = BASEHOST + "/mobile/AttachmentServlet?method=upload";

    public static final String PUTNEWS = BASEHOST + "/mobile/PostServlet?method=submit";

    public static final String PUTUSER = BASEHOST + "/mobile/VerifiedInfoServlet?method=submit";

    public static final String GETNEWS = BASEHOST + "/mobile/PostServlet?method=getAll";
}
