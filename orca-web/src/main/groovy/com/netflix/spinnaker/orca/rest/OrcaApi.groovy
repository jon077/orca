/*
 * Copyright 2014 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.netflix.spinnaker.orca.rest

import groovy.util.logging.Slf4j
import com.fasterxml.jackson.databind.ObjectMapper
import com.netflix.spinnaker.orca.pipeline.Pipeline
import com.netflix.spinnaker.orca.pipeline.PipelineStarter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.context.request.async.DeferredResult

@RestController
@Slf4j
class OrcaApi {

  @Autowired
  PipelineStarter pipelineStarter

  ObjectMapper mapper = new ObjectMapper()

  @RequestMapping(value = '/orchestrate', method = RequestMethod.POST)
  DeferredResult<Pipeline> orchestrate(@RequestBody Map pipeline) {
    log.info("starting pipeline {} for application {}", pipeline.name, pipeline.application)
    String stageJson = mapper.writeValueAsString(pipeline.stages.findAll { it.type != 'jenkins' })
    def q = new DeferredResult<Pipeline>()
    pipelineStarter.start(stageJson).subscribe {
      q.setResult(it)
    }
  }

  @RequestMapping(value = '/status/{id}', method = RequestMethod.GET)
  String status(@PathVariable String id) {
    '{"status":"Not even implemented at all"}'
  }
}
