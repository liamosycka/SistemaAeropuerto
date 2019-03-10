/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TrenInterno implements Runnable {

	private Terminal terminalActual;
	private Terminal terminalInicio;
	private Terminal[] arrTerminales;
	private Lock lock;
	private Condition esperandoTren, enTren, esperandoParaSubir, trenEspera, esperaTerminalA, esperaTerminalB,
			esperaTerminalC, esperaTerminalD;
	private int cantMax, cantActual, cantTerminalA, cantTerminalB, cantTerminalC, cantTerminalD, cantPersonasQueBajan;
	private CyclicBarrier barrera;
	private Semaphore semContinuar;

	public TrenInterno(int cantMax, Terminal[] arrTerminales) {
		this.terminalInicio = new Terminal('Z');
		this.terminalActual = terminalInicio;
		this.lock = new ReentrantLock(true);
		this.esperandoTren = lock.newCondition();
		this.enTren = lock.newCondition();
		this.esperandoParaSubir = lock.newCondition();
		this.trenEspera = lock.newCondition();
		this.esperaTerminalA = lock.newCondition();
		this.esperaTerminalB = lock.newCondition();
		this.esperaTerminalC = lock.newCondition();
		this.esperaTerminalD = lock.newCondition();
		this.cantMax = cantMax;
		this.barrera = new CyclicBarrier(cantMax + 1);
		this.arrTerminales = arrTerminales;
		this.semContinuar = new Semaphore(0, true);
		this.cantPersonasQueBajan = 0;
		this.cantActual = 0;
	}

	public void run() {
		while (true) {
			int pos = 0;
			this.comenzarRecorrido();
			while (pos < arrTerminales.length) {
				try {
					Thread.sleep(2000);
					this.mover(pos);
					Thread.sleep(2000);
					this.continuar();

				} catch (InterruptedException ex) {
					Logger.getLogger(TrenInterno.class.getName()).log(Level.SEVERE, null, ex);
				}
				pos++;

			}
			lock.lock();
			this.cantTerminalA=0;
			this.cantTerminalB=0;
			this.cantTerminalC=0;
			this.cantTerminalD=0;
			terminalActual = terminalInicio;
			System.out.println("        El tren ha regresado al inicio");
			this.esperandoParaSubir.signalAll();
			this.esperandoTren.signalAll();
			lock.unlock();
		}
	}

	public void esperarTren(Pasajero pasajero) {
		this.lock.lock();
		try {
			while (terminalActual.getLetraTerminal() != 'Z') {
				try {
					esperandoTren.await();
				} catch (InterruptedException ex) {
					Logger.getLogger(TrenInterno.class.getName()).log(Level.SEVERE, null, ex);
				}

			}

		} finally {
			this.lock.unlock();
		}

	}

	public void subir(Pasajero pasajero) {
		lock.lock();
		try {
			while (cantActual == cantMax) {
				try {
					esperandoParaSubir.await();
				} catch (InterruptedException ex) {
					Logger.getLogger(TrenInterno.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
			System.out.println("        Se ha subido al tren el pasajero " + pasajero.getId());
			switch (pasajero.getPasaje().getTerminal().getLetraTerminal()) {
			case 'A':
				this.cantTerminalA++;
				break;
			case 'B':
				this.cantTerminalB++;
				break;
			case 'C':
				this.cantTerminalC++;
				break;
			case 'D':
				this.cantTerminalD++;
				break;
			}
			this.cantActual++;
			System.out.println("cant actual " + cantActual + " cantMAx: " + cantMax);
		} finally {
			lock.unlock();
		}
		try {
			barrera.await();
		} catch (InterruptedException ex) {
			Logger.getLogger(TrenInterno.class.getName()).log(Level.SEVERE, null, ex);
		} catch (BrokenBarrierException ex) {
			Logger.getLogger(TrenInterno.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void mover(int pos) {
		semContinuar.release();
		this.lock.lock();
		System.out.println("----------TREN EN MOVIMIENTO----------");
		try {
			terminalActual = arrTerminales[pos];
			System.out.println(
					"||||||||||Tren ha llegado a terminal: " + terminalActual.getLetraTerminal() + "||||||||||");
			switch (pos) {
			case 0:
				this.cantPersonasQueBajan = cantTerminalA;
				this.esperaTerminalA.signalAll();
				break;
			case 1:
				this.cantPersonasQueBajan = cantTerminalB;
				this.esperaTerminalB.signalAll();
				break;
			case 2:
				this.cantPersonasQueBajan = cantTerminalC;
				this.esperaTerminalC.signalAll();
				break;
			case 3:
				this.cantPersonasQueBajan = cantTerminalD;
				this.esperaTerminalD.signalAll();
				break;
			}

		} finally {
			this.lock.unlock();
		}
	}

	private void continuar() {
		try {
			semContinuar.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void comenzarRecorrido() {
		try {
			barrera.await();
		} catch (InterruptedException ex) {
			Logger.getLogger(TrenInterno.class.getName()).log(Level.SEVERE, null, ex);
		} catch (BrokenBarrierException ex) {
			Logger.getLogger(TrenInterno.class.getName()).log(Level.SEVERE, null, ex);
		}
		System.out.println("----------Tren comenzo recorrido----------");

	}

	public void trasladarseATerminal(Pasajero pasajero, char terminalDeseada) {
		this.lock.lock();
		try {
			while (terminalActual.getLetraTerminal() != terminalDeseada) {
				try {
					switch (terminalDeseada) {
					case 'A':
						esperaTerminalA.await();
						break;
					case 'B':
						esperaTerminalB.await();
						break;
					case 'C':
						esperaTerminalC.await();
						break;
					case 'D':
						esperaTerminalD.await();
						break;
					}
				} catch (InterruptedException ex) {
					Logger.getLogger(TrenInterno.class.getName()).log(Level.SEVERE, null, ex);
				}
			}

			try {
				switch (terminalDeseada) {
				/*
				 * El primer pasajero que decide bajar es el responsable de adquirir el permiso
				 * del semaforo para que el tren no se vaya a la siguiente terminal.
				 */
				case 'A':
					if (cantPersonasQueBajan == cantTerminalA) {
						System.out
								.println("||||||||||Pasajero " + pasajero.getId() + " avisa que va a bajar||||||||||");
						semContinuar.acquire();

					}
					break;
				case 'B':
					if (cantPersonasQueBajan == cantTerminalB) {
						System.out
								.println("||||||||||Pasajero " + pasajero.getId() + " avisa que va a bajar||||||||||");
						semContinuar.acquire();
					}
					break;
				case 'C':
					if (cantPersonasQueBajan == cantTerminalC) {
						System.out
								.println("||||||||||Pasajero " + pasajero.getId() + " avisa que va a bajar||||||||||");
						semContinuar.acquire();
					}
					break;
				case 'D':
					if (cantPersonasQueBajan == cantTerminalD) {
						System.out
								.println("||||||||||Pasajero " + pasajero.getId() + " avisa que va a bajar||||||||||");
						semContinuar.acquire();
					}
					break;
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cantPersonasQueBajan--;

		} finally {
			this.lock.unlock();
		}
	}

	public void bajar(Pasajero pasajero) {
		this.lock.lock();
		this.cantActual--;
		System.out.println("en bajar, cant actual : " + cantActual);
		System.out.println("        Se ha bajado del tren el pasajero " + pasajero.getId());
		if (cantPersonasQueBajan == 0) {
			// es la ultima persona que baja en esa terminal
			System.out.println("|||||||||||Se ha bajado la ultima persona en la terminal "
					+ this.terminalActual.getLetraTerminal() + "||||||||||");
			semContinuar.release();
		}
		this.lock.unlock();
		this.terminalActual.ingresarTerminal(pasajero);
	}
}
