/*
 * This file is part of eCobertura.
 * 
 * Copyright (c) 2009, 2010 Joachim Hofer
 * All rights reserved.
 *
 * eCobertura is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *  
 * eCobertura is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with eCobertura.  If not, see <http://www.gnu.org/licenses/>.
 */
package ecobertura.ui.views.session.commands

import org.eclipse.core.commands._
import org.eclipse.swt.SWT
import org.eclipse.swt.widgets.FileDialog
import org.eclipse.ui.handlers.HandlerUtil

import ecobertura.core.cobertura.CoberturaWrapper
import ecobertura.core.data.CoverageSession
import ecobertura.ui.views.session.CoverageSessionModel

class OpenCoverageSessionHandler extends AbstractHandler {
	override def execute(event: ExecutionEvent) = {
		val sessionFilename = retrieveCoverageSessionFilename(event)
		val projectData = CoberturaWrapper.get.projectDataFromFile(sessionFilename)
		CoverageSessionModel.get.addCoverageSession(
				CoverageSession.fromCoberturaProjectData(projectData))
		
		null // handlers must return null
	}
	
	private def retrieveCoverageSessionFilename(event: ExecutionEvent) = {
		val parentShell = HandlerUtil.getActiveShell(event)
		val dialog = new FileDialog(parentShell, SWT.OPEN)
		dialog.setFilterNames(Array("Cobertura Session Files (*.ser)", "All Files (*)"))
		dialog.setFilterExtensions(Array("*.ser", "*"))
		
		dialog.open
	}
}
