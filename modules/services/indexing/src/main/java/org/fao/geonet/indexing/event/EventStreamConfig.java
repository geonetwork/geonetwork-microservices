/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.indexing.event;

import org.springframework.cloud.stream.annotation.EnableBinding;

@EnableBinding(EventStream.class)
public class EventStreamConfig {
}