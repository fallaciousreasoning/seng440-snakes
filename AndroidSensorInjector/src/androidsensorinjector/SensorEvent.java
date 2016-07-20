/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package androidsensorinjector;

/**
 *
 * @author comqdhb
 */
public class SensorEvent {
    private final int accuracy;
    private final int sensor;
    private final long timestamp;
    private final float[] values;
    private final String type;
    
    public SensorEvent(String type,int accuracy,int sensor,long timestamp,float[] v){
        this.type=type;
        this.accuracy=accuracy;
        this.sensor=sensor;
        this.timestamp=timestamp;
        this.values=v;
    }
    
    public String json(){
        StringBuilder sb=new StringBuilder();
        sb.append("{");
        
        sb.append("\"TYPE\":\""+type+"\",");
        
        sb.append("\"accuracy\":"+accuracy+",");
        sb.append("\"sensor\":"+sensor+",");
        sb.append("\"timestamp\":"+timestamp+",");
        sb.append("\"values\":[");
        boolean isFirst=true;
        for (float f:values){
            sb.append( isFirst?"":",");
            isFirst=false;
            sb.append(f);
        }
        sb.append("]");
        sb.append("}");
        return sb.toString();
    }
}
