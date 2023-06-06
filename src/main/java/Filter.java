public class Filter {

    private String valueFromFile;

    private int columnNumber;

    private String evalSign;

    private String valueWithCompareTo;

    public Filter(String filter) {
        parse(filter);
    }

    public void parse(String filter) {
        int indexStartColumnNumber = filter.indexOf("column[") + ("column[".length());
        int indexEndColumnNumber = indexStartColumnNumber;
        StringBuilder columnNumberBuilder = new StringBuilder();
        try {
            for (char c : filter.substring(indexStartColumnNumber).toCharArray()) {

                if (c == ']') {
                    columnNumber = Integer.parseInt(columnNumberBuilder.toString());
                    indexEndColumnNumber++;
                    break;
                } else {
                    columnNumberBuilder.append(c);
                }
                indexEndColumnNumber++;
            }
            evalSign = String.valueOf(filter.charAt(indexEndColumnNumber));
            if (filter.charAt(indexEndColumnNumber + 1) == '>') {
                evalSign = filter.substring(indexEndColumnNumber, indexEndColumnNumber + 2);
            }
            valueWithCompareTo = filter.substring(filter.indexOf(evalSign) + evalSign.length());
        }
        catch (Exception e) {
            System.out.println("Введенные данные не соответствуют формату.");
        }
    }
    public boolean calculate() {
        int result;
        if(valueWithCompareTo.contains("'")) {
            valueWithCompareTo = valueWithCompareTo.replaceAll("'", "\"");
            result = valueFromFile.compareToIgnoreCase(valueWithCompareTo);
        }
        else if(valueFromFile.contains(".")) {
            try {
                double to = Double.parseDouble(valueWithCompareTo);
                result = Double.compare(Double.parseDouble(valueFromFile), to);
            }
            catch (NumberFormatException e) {
                throw new RuntimeException();
            }
        }
        else {
            try {
                int to = Integer.parseInt(valueWithCompareTo);
                result = Integer.compare(Integer.parseInt(valueFromFile), to);
            }
            catch (NumberFormatException e) {
                throw new RuntimeException();
            }
        }
        if (result > 0) {
            return evalSign.equals(">") || evalSign.equals("<>");
        }
        else if(result == 0) {
            return evalSign.equals("=");
        }
        else {
            return evalSign.equals("<") || evalSign.equals("<>");
        }
    }

    public String getValueFromFile() {
        return valueFromFile;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    public String getEvalSign() {
        return evalSign;
    }

    public String getValueWithCompareTo() {
        return valueWithCompareTo;
    }

    public void setValueFromFile(String valueFromFile) {
        this.valueFromFile = valueFromFile;
    }

    public void setColumnNumber(int columnNumber) {
        this.columnNumber = columnNumber;
    }

    public void setEvalSign(String evalSign) {
        this.evalSign = evalSign;
    }

    public void setValueWithCompareTo(String valueWithCompareTo) {
        this.valueWithCompareTo = valueWithCompareTo;
    }
}
