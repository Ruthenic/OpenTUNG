package de.ecconia.java.opentung.inputs;

import de.ecconia.java.opentung.core.SimpleCallback;
import org.lwjgl.glfw.GLFW;

public class InputReceiver
{
	private final InputProcessor processor;
	private final Thread inputThread;
	
	private boolean intervalMode;
	
	public InputReceiver(InputProcessor processor, long windowID)
	{
		this.processor = processor;
		
		GLFW.glfwSetCursorPosCallback(windowID, (windowIDC, x, y) -> {
			processor.updateCursorPosition((int) x, (int) y);
		});
		
		GLFW.glfwSetMouseButtonCallback(windowID, (windowIDC, button, action, mods) -> {
			if(action == GLFW.GLFW_RELEASE)
			{
				processor.cursorReleased(button);
			}
			else if(action == GLFW.GLFW_PRESS)
			{
				processor.cursorPressed(button);
			}
		});
		
		GLFW.glfwSetKeyCallback(windowID, (windowIDC, key, scancode, action, mods) -> {
			if(action == GLFW.GLFW_PRESS)
			{
				processor.keyPressed(key, scancode, mods);
			}
			else if(action == GLFW.GLFW_RELEASE)
			{
				processor.keyReleased(key, scancode, mods);
			}
		});
		
		GLFW.glfwSetWindowFocusCallback(windowID, (windowIDC, state) -> {
			processor.focusChanged(state);
		});
		
		GLFW.glfwSetScrollCallback(windowID, (windowIDC, xoffset, yoffset) -> {
			processor.mouseScrolled(xoffset, yoffset);
		});
		
		inputThread = Thread.currentThread();
	}
	
	public void stop()
	{
		inputThread.interrupt();
		GLFW.glfwPostEmptyEvent();
	}
	
	public void eventPollEntry(SimpleCallback titleUpdater)
	{
		long time = System.currentTimeMillis();
		long lastTitleUpdate = 0;
		while(!Thread.currentThread().isInterrupted())
		{
			if(intervalMode)
			{
				GLFW.glfwPollEvents();
				
				processor.postEvents();
				
				try
				{
					//Calculates 'wTime', the time since the last execution... But dunno how it should be used properly.
					long cTime = System.currentTimeMillis();
					long wTime = cTime - time;
					time = cTime;
					if(wTime > 0)
					{
						Thread.sleep(10);
					}
				}
				catch(InterruptedException e)
				{
					break;
				}
			}
			else
			{
				GLFW.glfwWaitEventsTimeout(1D); //Wait the maximum of 1 second.
				processor.postEvents(); //Call anyway.
				time = System.currentTimeMillis(); //Keep track of time, in case of switching.
			}
			
			{
				long now = System.currentTimeMillis();
				if(now - lastTitleUpdate > 1000L)
				{
					titleUpdater.call();
					lastTitleUpdate = now;
				}
			}
		}
	}
	
	public void setIntervalMode(boolean intervalMode)
	{
		this.intervalMode = intervalMode;
	}
}
