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
public interface RemoteEventListener {
    public void receiveData(String data);
    public void ended();
}
