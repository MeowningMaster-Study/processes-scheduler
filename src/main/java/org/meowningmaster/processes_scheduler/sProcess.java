package org.meowningmaster.processes_scheduler;

public class sProcess {
  public int index;
  /**
   * The time the process should run to complete
   */
  public int cputime;
  /**
   * The time between I/O blocks of the process
   */
  public int ioblocking;
  /**
   * The time the process worked
   */
  public int cpudone;
  /**
   * Counter for I/O blocks
   */
  public int ionext;
  /**
   * Number of times the process was I/O blocked
   */
  public int numblocked;
  /**
   * Priority weight
   */
  public int weight = 2;

  public sProcess (int index, int cputime, int ioblocking, int cpudone, int ionext, int numblocked) {
    this.index = index;
    this.cputime = cputime;
    this.ioblocking = ioblocking;
    this.cpudone = cpudone;
    this.ionext = ionext;
    this.numblocked = numblocked;
  }

  public String toString() {
    return String.format("done: %d/%d, blocking interval: %d, times blocked: %d", cpudone, cputime, ioblocking, numblocked);
  }

  public boolean isCompleted() {
    return cpudone >= cputime;
  }

  public boolean isIOBlocked() {
    return ionext >= ioblocking;
  }
}
