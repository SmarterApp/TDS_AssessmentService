package tds.accommodation;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents an accommodation/tool dependency
 */
public class Dependency {
    private String context;
    private String ifType;
    private String ifValue;
    private String thenType;
    private String thenValue;
    
    @JsonProperty("default")
    private boolean isDefault;
    
    /**
     * Empty constructor for frameworks
     */
    private Dependency() {}
    
    public Dependency(Builder builder) {
        this.context = builder.context;
        this.ifType = builder.ifType;
        this.ifValue = builder.ifValue;
        this.thenType = builder.thenType;
        this.thenValue = builder.thenValue;
        this.isDefault = builder.isDefault;
    }
    
    /**
     * @return The context of the dependency (either segment id or assessment id)
     */
    public String getContext() {
        return context;
    }
    
    /**
     * @return the "type" conditional for this dependency
     */
    public String getIfType() {
        return ifType;
    }
    
    /**
     * @return the "value" or code conditional for this dependency
     */
    public String getIfValue() {
        return ifValue;
    }
    
    /**
     * @return if the "if" dependencies are satisfied, then this is the "type" that is valid
     */
    public String getThenType() {
        return thenType;
    }
    
    /**
     * @return if the "if" dependencies are satisfied, then this is the "value" or code that is valid
     */
    public String getThenValue() {
        return thenValue;
    }
    
    /**
     * @return flag specifying whether this is the default value for the dependency
     */
    public boolean isDefault() {
        return isDefault;
    }

    public static final class Builder {
        private String context;
        private String ifType;
        private String ifValue;
        private String thenType;
        private String thenValue;
        private boolean isDefault;
        
        public Builder(String context) {
            this.context = context;
        }
        
        public Builder withIfType(String ifType) {
            this.ifType = ifType;
            return this;
        }
        
        public Builder withIfValue(String ifValue) {
            this.ifValue = ifValue;
            return this;
        }
        
        public Builder withThenType(String thenType) {
            this.thenType = thenType;
            return this;
        }
        
        public Builder withThenValue(String thenValue) {
            this.thenValue = thenValue;
            return this;
        }
        
        public Builder withIsDefault(boolean isDefault) {
            this.isDefault = isDefault;
            return this;
        }
        
        public Dependency build() {
            return new Dependency(this);
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Dependency that = (Dependency) o;
        
        if (isDefault != that.isDefault) return false;
        if (!context.equals(that.context)) return false;
        if (!ifType.equals(that.ifType)) return false;
        if (!ifValue.equals(that.ifValue)) return false;
        if (!thenType.equals(that.thenType)) return false;
        return thenValue.equals(that.thenValue);
    }
    
    @Override
    public int hashCode() {
        int result = context.hashCode();
        result = 31 * result + ifType.hashCode();
        result = 31 * result + ifValue.hashCode();
        result = 31 * result + thenType.hashCode();
        result = 31 * result + thenValue.hashCode();
        result = 31 * result + (isDefault ? 1 : 0);
        return result;
    }
}
