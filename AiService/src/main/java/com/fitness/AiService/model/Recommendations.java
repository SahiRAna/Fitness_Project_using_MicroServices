package com.fitness.AiService.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a recommendation entity.
 * This class is a MongoDB document that stores recommendation data.
 *
 * <h2>Fields</h2>
 * <table border="1">
 *     <tr>
 *         <th>Field</th>
 *         <th>Type</th>
 *         <th>Description</th>
 *     </tr>
 *     <tr>
 *         <td>id</td>
 *         <td>String</td>
 *         <td>The unique identifier for the recommendation.</td>
 *     </tr>
 *     <tr>
 *         <td>activityId</td>
 *         <td>String</td>
 *         <td>The ID of the activity associated with the recommendation.</td>
 *     </tr>
 *     <tr>
 *         <td>userId</td>
 *         <td>String</td>
 *         <td>The ID of the user for whom the recommendation is made.</td>
 *     </tr>
 *     <tr>
 *         <td>activityType</td>
 *         <td>String</td>
 *         <td>The type of activity for which the recommendation is made.</td>
 *     </tr>
 *     <tr>
 *         <td>recommendations</td>
 *         <td>String</td>
 *         <td>The main recommendation text.</td>
 *     </tr>
 *     <tr>
 *         <td>improvements</td>
 *         <td>List&lt;String&gt;</td>
 *         <td>A list of suggested improvements.</td>
 *     </tr>
 *     <tr>
 *         <td>suggestions</td>
 *         <td>List&lt;String&gt;</td>
 *         <td>A list of general suggestions.</td>
 *     </tr>
 *     <tr>
 *         <td>safety</td>
 *         <td>List&lt;String&gt;</td>
 *         <td>A list of safety precautions.</td>
 *     </tr>
 *     <tr>
 *         <td>createdAt</td>
 *         <td>LocalDateTime</td>
 *         <td>The timestamp when the recommendation was created.</td>
 *     </tr>
 * </table>
 */
@Document(collection = "recommendations")
@Data
@Builder
public class Recommendations {

    /**
     * The unique identifier for the recommendation.
     */
    @Id
    private String id;

    /**
     * The ID of the activity associated with the recommendation.
     */
    private String activityId;

    /**
     * The ID of the user for whom the recommendation is made.
     */
    private String userId;

    /**
     * The type of activity for which the recommendation is made.
     */
    private String activityType;

    /**
     * The main recommendation text.
     */
    private String recommendations;

    /**
     * A list of suggested improvements.
     */
    private List<String> improvements;

    /**
     * A list of general suggestions.
     */
    private List<String> suggestions;

    /**
     * A list of safety precautions.
     */
    private List<String> safety;

    /**
     * The timestamp when the recommendation was created.
     */
    @CreatedDate
    private LocalDateTime createdAt;

}
