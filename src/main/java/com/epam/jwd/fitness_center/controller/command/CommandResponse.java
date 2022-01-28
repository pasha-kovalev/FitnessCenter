package com.epam.jwd.fitness_center.controller.command;

/** Interface of response
 *
 * @author Pavel Kovalev
 * @version 1.0
 */
public interface CommandResponse {
    /** Gets is response should be redirected
     * @return true if the response should be redirected
     */
    boolean isRedirect();

    /**Gets is it ajax response
     * @return true if the response should be AJAX
     */
    boolean isAjax();

    /**Gets the path page
     * @return string representation of path
     */
    String getPath();

    /**Gets the ajax data
     * @return string representation of ajax data
     */
    String getAjaxData();
}
