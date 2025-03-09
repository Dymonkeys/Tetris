/*
 * File: Copyable.java
 * Author: David Neufeld
 * Created Date: Fri Sep 16 2022 at 8:52:31 PM
 * E-mail: david.neufeld@maine.edu
 * Description:
 * 
 * Collaboration: 
 * 
 */
package Tetris;
public interface Copyable<T extends Copyable<T>>
{
  public T copy();
}
