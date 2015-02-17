package edu.ucla.cs.cs144;

public class SearchQuery {
    private String query;
    private int numResultsToSkip;
    private int numResultsToReturn;

	public SearchQuery() {}

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query =  query;
    }

    public int getNumResultsToSkip() {
        return numResultsToSkip;
    }

    public void setNumResultsToSkip(int numResultsToSkip) {
        this.numResultsToSkip = numResultsToSkip;
    }

    public int getNumResultsToReturn() {
        return numResultsToReturn;
    }

    public void setNumResultsToReturn(int numResultsToReturn) {
        this.numResultsToReturn = numResultsToReturn;
    }
}
