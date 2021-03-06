/*
 * This file is part of eCobertura.
 * 
 * Copyright (c) 2009, 2010 Joachim Hofer
 * All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package ecobertura.core.log

import org.eclipse.core.runtime._
import org.junit._
import org.junit.Assert._
import org.osgi.framework.Bundle;

import ecobertura.core.CorePlugin

class LoggerPDETest extends ILogListener {
	private var core: Bundle = null
	private var logSeverity: Int = -1
	private var logMessage: String = null
	private var logThrowable: Throwable = null
	
	@Before
	def setUp = {
		core = Platform getBundle CorePlugin.pluginId
		Platform addLogListener this
		core.start
	}
	
	@After
	def tearDown = {
		Platform removeLogListener this
		core.stop
	}
	
	@Test
	def testInfoString = {
		shouldLog(IStatus.INFO, "hello world", null)
		EclipseLogger info "hello world"
	}
	
	@Test
	def testInfoThrowable = {
		shouldLog(IStatus.INFO, "hello", new Exception("hello"))
		EclipseLogger info new Exception("hello")
	}
	
	@Test
	def testInfoStringThrowable = {
		shouldLog(IStatus.INFO, "hello world", new Exception("hello"))
		EclipseLogger info ("hello world", new Exception("hello"))
	}

	@Test
	def testWarnString = {
		shouldLog(IStatus.WARNING, "hello", null)
		EclipseLogger warn "hello"
	}
	
	@Test
	def testWarnThrowable = {
		shouldLog(IStatus.WARNING, "helloExc", new Exception("helloExc"))
		EclipseLogger warn new Exception("helloExc")
	}
	
	@Test
	def testWarnStringThrowable = {
		shouldLog(IStatus.WARNING, "helloMsg", new Exception("helloExc"))
		EclipseLogger warn ("helloMsg", new Exception("helloExc"))
	}

	@Test
	def testErrorString = {
		shouldLog(IStatus.ERROR, "", null)
		EclipseLogger error ""
	}
	
	@Test
	def testErrorThrowable = {
		shouldLog(IStatus.ERROR, "rte", new RuntimeException("rte"))
		EclipseLogger error new RuntimeException("rte")
	}
	
	@Test
	def testErrorStringThrowable = {
		shouldLog(IStatus.ERROR, "rte msg", new RuntimeException("rte"))
		EclipseLogger error ("rte msg", new RuntimeException("rte"))
	}

	private def shouldLog(severity: Int, message: String, throwable: Throwable) = {
		logSeverity = severity
		logMessage = message
		logThrowable = throwable
	}
	
	override def logging(status: IStatus, plugin: String) = {
		assertSame(logSeverity, status.getSeverity)
		assertEquals(logMessage, status.getMessage)
		if (logThrowable == null) assertNull(status.getException)
		else {
			assertEquals(logThrowable.getMessage, status.getException.getMessage)
			assertEquals(logThrowable.getClass, status.getException.getClass)
			assertEquals(logThrowable.getCause, status.getException.getCause)
		}
	}
}