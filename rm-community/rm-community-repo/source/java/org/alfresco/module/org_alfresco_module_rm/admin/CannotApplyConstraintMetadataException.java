 
package org.alfresco.module.org_alfresco_module_rm.admin;

import org.alfresco.service.namespace.QName;
import org.springframework.extensions.surf.util.I18NUtil;

/**
 * Custom metadata exception.
 * 
 * @author Roy Wethearll
 * @since 2.1
 * @see org.alfresco.module.org_alfresco_module_rm.CannotApplyConstraintMetadataException
 */
public class CannotApplyConstraintMetadataException extends CustomMetadataException
{
    private static final long serialVersionUID = -6194867814140009959L;
    public static final String MSG_CANNOT_APPLY_CONSTRAINT = "rm.admin.cannot-apply-constraint";
    
    public CannotApplyConstraintMetadataException(QName lovConstraint, String propIdAsString, QName dataType)
    {
        super(I18NUtil.getMessage(CannotApplyConstraintMetadataException.MSG_CANNOT_APPLY_CONSTRAINT, lovConstraint, propIdAsString, dataType));
    }
}
