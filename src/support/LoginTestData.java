package support;

public record LoginTestData(String id, String username, String password, ExpectedResult expectedResult) {
    @Override
    public String toString() {
        return id;
    }
}
