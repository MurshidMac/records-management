 
package org.alfresco.module.org_alfresco_module_rm.patch.v22;

import org.alfresco.module.org_alfresco_module_rm.dod5015.DOD5015Model;
import org.alfresco.module.org_alfresco_module_rm.model.RecordsManagementModel;
import org.alfresco.module.org_alfresco_module_rm.patch.AbstractModulePatch;
import org.alfresco.repo.domain.qname.QNameDAO;

/**
 * DOD compliant site patch.
 * 
 * Makes all existing sites of type dod:site to preserve their DOD compliance status.
 * 
 * @author Roy Wetherall
 * @since 2.2
 */
public class RMv22DODCompliantSitePatch extends AbstractModulePatch 
                                        implements RecordsManagementModel
{
    /** QName DAO */
    private QNameDAO qnameDAO;
    
    /** indicates whether we convert to a standard file plan or not */
    private boolean convertToStandardFilePlan = false;

    /**
     * @param qnameDAO  QName DAO
     */
    public void setQnameDAO(QNameDAO qnameDAO)
    {
        this.qnameDAO = qnameDAO;
    }
    
    /**
     * @param convertToStandardFilePlan	convert to standard file if true, false otherwise
     */
    public void setConvertToStandardFilePlan(boolean convertToStandardFilePlan) 
    {
		this.convertToStandardFilePlan = convertToStandardFilePlan;
	}
    
    /**
     * @see org.alfresco.module.org_alfresco_module_rm.patch.AbstractModulePatch#applyInternal()
     */
    @Override
    public void applyInternal()
    {
    	if (!convertToStandardFilePlan)
    	{
	        // ensure all existing sites are of the correct type
	        if (qnameDAO.getQName(RecordsManagementModel.TYPE_RM_SITE) != null &&
	            qnameDAO.getQName(DOD5015Model.TYPE_DOD_5015_SITE) == null)
	        {
	            qnameDAO.updateQName(RecordsManagementModel.TYPE_RM_SITE, DOD5015Model.TYPE_DOD_5015_SITE);
	        }
	        
	        // ensure all the existing file plans are of the correct type
	        if (qnameDAO.getQName(RecordsManagementModel.TYPE_FILE_PLAN) != null &&
	            qnameDAO.getQName(DOD5015Model.TYPE_DOD_5015_FILE_PLAN) == null)
	        {
	            qnameDAO.updateQName(RecordsManagementModel.TYPE_FILE_PLAN, DOD5015Model.TYPE_DOD_5015_FILE_PLAN);
	        }
    	}
    }
}
