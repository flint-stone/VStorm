/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package storm.vstorm;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.BoltDeclarer;
import backtype.storm.topology.TopologyBuilder;
import storm.vstorm.bolt.ClientQOECollect;
import storm.vstorm.bolt.FinalCollectSink;
import storm.vstorm.bolt.FrameRateAdjustBolt;
import storm.vstorm.bolt.ServerChangeSink;
import storm.vstorm.bolt.ServerSearchingBolt;
import storm.vstorm.spout.ClientStatRenderSpout;



/**
 * This is a basic example of a IMG topology.
 */
public class ClientSideAdaptionTopology {


	public static void main(String[] args) throws Exception {
		int paralellism = 2;

		TopologyBuilder builder = new TopologyBuilder();

		builder.setSpout("ClientStatRender_spout", new ClientStatRenderSpout(), paralellism);

		builder.setBolt("ClientQOECalculation_bolt", new ClientQOECollect(), paralellism).shuffleGrouping("ClientStatRender_spout");
		builder.setBolt("FrameRateAdjust_bolt", new FrameRateAdjustBolt(), paralellism).shuffleGrouping("ClientQOECalculation_bolt");
		BoltDeclarer output1 = builder.setBolt("FinalCollect_sink", new FinalCollectSink(), paralellism);
		output1.shuffleGrouping("ClientQOECalculation_bolt");
		output1.shuffleGrouping("FrameRateAdjust_bolt");
		builder.setBolt("ServerSearching_bolt", new ServerSearchingBolt(), paralellism).shuffleGrouping("FrameRateAdjust_bolt");
		builder.setBolt("ServerChange_sink", new ServerChangeSink(), paralellism).shuffleGrouping("ServerSearching_bolt");
		
		Config conf = new Config();
		conf.setDebug(true);

		conf.setNumAckers(0);

		conf.setNumWorkers(4);

		StormSubmitter.submitTopologyWithProgressBar(args[0], conf, builder.createTopology());

	}
}