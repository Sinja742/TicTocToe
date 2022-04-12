public enum Symbol {
    CROSS("X"),
    CIRCLE("O");

    private final String symbol;

    Symbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return this.symbol;
    }

}
