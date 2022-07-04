package ellemes.expandedstorage.registration;

public interface ContentConsumer {
    void accept(Content content);

    default ContentConsumer andThen(ContentConsumer after) {
        return (content) -> {
            accept(content);
            after.accept(content);
        };
    }

    default ContentConsumer andThenIf(boolean condition, ContentConsumer after) {
        return condition ? andThen(after) : this;
    }
}
