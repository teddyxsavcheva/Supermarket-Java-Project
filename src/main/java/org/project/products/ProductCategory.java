package org.project.products;

public enum ProductCategory {
    NUTRITION(1), NON_NUTRITION(0);
    private final int codeForPriority;

    ProductCategory(int codeForPriority) {
        this.codeForPriority = codeForPriority;
    }

    public int getCodeForPriority() {
        return codeForPriority;
    }
}
