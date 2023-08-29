package model;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * class to represent a driver history
 */
public class DriverHistory extends Component {
    private List<Violation> violations;

    public void setViolations(List<Violation> violations) {
        this.violations = violations;
    }

    public List<Violation> getViolations() {
        return violations;
    }
}
