package com.academia.gorillas;

public class Config {

    public static String URL_HOST = "https://academia.gorila.com.py";
    public static int HEADLINE_CATEGORY_ID = 13; // Your Categories -> www.YOUR_WORDPRESS_SITE/wp-json/wp/v2/categories;
    public static String CATEGORY_FILTER = "3";
    public static String POST_FILTER = "CAPECO";
    public static String URL_HEADLINE_POSTS = URL_HOST + "/wp-json/wp/v2/posts?_embed&status=publish&categories=" + String.valueOf(HEADLINE_CATEGORY_ID);
    public static String URL_POSTS = URL_HOST + "/wp-json/wp/v2/posts?_embed&status=publish&tag=" + POST_FILTER;
    public static String URL_COMMENTS = URL_HOST + "/wp-json/wp/v2/comments";
    public static String URL_POST_COMMENTS = URL_HOST + "/wp-json/wp/v2/comments?status=approve&post=";
    public static String URL_POST_FCM = URL_HOST + "?api-fcm=register";
    public static String URL_CATEGORIES = URL_HOST + "/wp-json/wp/v2/categories?parent=" + String.valueOf(CATEGORY_FILTER);
    public static String URL_CATEGORY_POST = URL_HOST + "/wp-json/wp/v2/posts?_embed&status=publish&categories=";
    public static String URL_TAGS = URL_HOST + "/wp-json/wp/v2/tags?per_page=100";
    public static String URL_TAG_POST = URL_HOST + "/wp-json/wp/v2/posts?_embed&status=publish";
    public static String URL_PAGES = URL_HOST + "/wp-json/wp/v2/pages?_embed&status=publish";



}
