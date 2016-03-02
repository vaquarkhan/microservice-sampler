package com.soagrowers.productintegrationtests;

/**
 * Created by ben on 24/02/16.
 */
public class Statics {

    public static final int PORT_FOR_COMMANDS = 9000;
    public static final int COMMAND_SIDE_MANAGEMENT_PORT_NUM = 9001;
    public static final int PORT_FOR_QUERIES = 9090;
    public static final int QUERY_SIDE_MANAGEMENT_PORT_NUM = 9091;


    public static final String API = "/api";
    public static final String VERSION = "/v1";
    public static final String PRODUCTS_CMD_BASE_PATH = API + VERSION + "/products";
    public static final String PRODUCTS_QRY_BASE_PATH = "/products";
    public static final String CMD_PRODUCT_ADD = "/add";

}