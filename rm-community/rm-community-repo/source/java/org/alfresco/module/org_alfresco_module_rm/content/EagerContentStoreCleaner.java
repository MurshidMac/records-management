 
package org.alfresco.module.org_alfresco_module_rm.content;

import java.io.File;
import java.util.Set;

import org.alfresco.module.org_alfresco_module_rm.content.cleanser.ContentCleanser;
import org.alfresco.module.org_alfresco_module_rm.util.TransactionalResourceHelper;
import org.alfresco.repo.content.ContentStore;
import org.alfresco.repo.content.filestore.FileContentReader;
import org.alfresco.service.cmr.repository.ContentReader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Eager content store cleaner that allows content to be registered for cleansing before
 * destruction.
 * 
 * @author Roy Wetherall
 * @since 2.4.a
 */
public class EagerContentStoreCleaner extends org.alfresco.repo.content.cleanup.EagerContentStoreCleaner
{
    /** transaction resource key */
    protected static final String KEY_POST_COMMIT_CLEANSING_URLS = "postCommitCleansingUrls";

    /** logger */
    private static Log logger = LogFactory.getLog(EagerContentStoreCleaner.class);
    
    /** transactional resource helper */
    private TransactionalResourceHelper transactionalResourceHelper;
    
    /** content cleanser */
    private ContentCleanser contentCleanser;
    
    /**
     * @param transactionResourceHelper transactional resource helper
     */
    public void setTransactionalResourceHelper(TransactionalResourceHelper transactionalResourceHelper)
    {
        this.transactionalResourceHelper = transactionalResourceHelper;
    }
    
    /**
     * @param contentCleanser   content cleanser
     */
    public void setContentCleanser(ContentCleanser contentCleanser)
    {
        this.contentCleanser = contentCleanser;
    }
    
    /**
     * Registers orphaned content URLs for cleansing
     * 
     * @param contentUrl    content url
     */
    public void registerOrphanedContentUrlForCleansing(String contentUrl)
    {
        // make note of content that needs cleansing
        Set<String> cleansingUrls = transactionalResourceHelper.getSet(KEY_POST_COMMIT_CLEANSING_URLS);
        cleansingUrls.add(contentUrl);
        
        // register as usual
        registerOrphanedContentUrl(contentUrl, true);
    }
    
    /**
     * @see org.alfresco.repo.content.cleanup.EagerContentStoreCleaner#deleteFromStore(java.lang.String, org.alfresco.repo.content.ContentStore)
     */
    @Override
    protected boolean deleteFromStore(String contentUrl, ContentStore store)
    {
        // determine if the content requires cleansing or not
        Set<String> cleansingUrls = transactionalResourceHelper.getSet(KEY_POST_COMMIT_CLEANSING_URLS);
        if (cleansingUrls.contains(contentUrl))
        {
            // cleanse content before delete
            cleanseContent(contentUrl, store);
        }
        
        // delete from store
        return super.deleteFromStore(contentUrl, store);
    }
    
    /**
     * Cleanse content
     * 
     * @param contentUrl    content url
     * @param store         content store
     */
    private void cleanseContent(String contentUrl, ContentStore store)
    {
        if (contentCleanser == null)
        {
            logger.error(
                        "No content cleanser specified.  Unable to cleanse: \n" +
                        "   URL:    " + contentUrl + "\n" +
                        "   Source: " + store);
        }
        else
        {
            // First check if the content is present at all
            ContentReader reader = store.getReader(contentUrl);
            if (reader != null && reader.exists())
            {
                // Call to implementation's shred
                if (logger.isDebugEnabled())
                {
                    logger.debug(
                            "About to cleanse: \n" +
                            "   URL:    " + contentUrl + "\n" +
                            "   Source: " + store);
                }
                try
                {
                    if (reader instanceof FileContentReader)
                    {
                        // get file content
                        FileContentReader fileReader = (FileContentReader) reader;
                        File file = fileReader.getFile();
                        
                        // cleanse content
                        contentCleanser.cleanse(file);
                    }
                }
                catch (Exception e)
                {
                    logger.error(
                            "Content cleansing failed: \n" +
                            "   URL:    " + contentUrl + "\n" +
                            "   Source: " + store + "\n" +
                            "   Reader: " + reader,
                            e);
                }
            }
            else
            {
                logger.error(
                        "Content no longer exists.  Unable to cleanse: \n" +
                        "   URL:    " + contentUrl + "\n" +
                        "   Source: " + store);
            }
        }
    }

}
