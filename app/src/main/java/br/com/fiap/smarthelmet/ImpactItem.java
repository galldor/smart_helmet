package br.com.fiap.smarthelmet;

public class ImpactItem {
    public final String id;
    public final String content;
    public final String details;

    public ImpactItem(String id, String content, String details) {
        this.id = id;
        this.content = content;
        this.details = details;
    }

    @Override
    public String toString() {
        return content;
    }
}
