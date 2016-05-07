/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bloqueiobifasico;

/**
 *
 * @author humbe
 */
public enum StatusDoDado {
    statusDesbloqueado(0),
    statusBloqueadoExclusivo(1),
    statusBloqueadoCompartilhado(2);

    private int status;

    private StatusDoDado() {
    }

    private StatusDoDado(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}
