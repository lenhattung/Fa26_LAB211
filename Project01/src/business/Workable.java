/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package business;

/**
 *
 * @author Le Nhat Tung
 */
public interface Workable<T> {
    public void addNew(T x);
    public void update(T x);
    public T searchById(String x);
    public void showAll();
}
