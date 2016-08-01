package androidsensorinjector;

import java.util.ArrayList;
import java.util.List;

/**
 * A class which generates the values to send to the accelerometer
 */
public class AccelerometerValuesBuilder implements SensorValueBuilder {
    @Override
    public List<float[]> getValues(Direction direction) {
        float[] values = new float[3];
        float magicNumber = 10;

        values[2] = magicNumber;

        switch (direction) {
            case NORTH:
                values[1] = -magicNumber;
                values[0] = 0;
                break;
            case SOUTH:
                values[1] = magicNumber;
                values[0] = 0;
                break;
            case EAST:
                values[0] = -magicNumber;
                values[1] = 0;
                break;
            case WEST:
                values[0] = magicNumber;
                values[1] = 0;
                break;
        }

        List<float[]> valuesList = new ArrayList<>();
        valuesList.add(values);
        return valuesList;
    }
}
