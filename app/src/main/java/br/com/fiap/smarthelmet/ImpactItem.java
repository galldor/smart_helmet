package br.com.fiap.smarthelmet;

public class ImpactItem {
    public final String id;
    public final String content;
    public final Double details;

    public ImpactItem(String id, String content, Double details) {
        this.id = id;
        this.content = content;
        this.details = details;
    }

    @Override
    public String toString() {
        return content;
    }
}
