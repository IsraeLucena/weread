package com.example.israel.rssread.Model;

public class FeedRss {
    private String nome;
    private String url;
    private String key;

    public FeedRss() {
    }

    public FeedRss(String nome, String url) {
        this.nome = nome;
        this.url = url;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
