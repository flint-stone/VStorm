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
package storm.vstorm.spout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Random;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

public class ClientStatRenderSpout extends BaseRichSpout {
  SpoutOutputCollector _collector;
  Random _rand;


  @Override
  public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
    _collector = collector;
    _rand = new Random();
  }

  @Override
  public void nextTuple() {
    Utils.sleep(5000);
    //String[] sentences = new String[]{ "the cow jumped over the moon", "an apple a day keeps the doctor away",
     //   "four score and seven years ago", "snow white and the seven dwarfs", "i am at two with nature" };
    //String sentence = sentences[_rand.nextInt(sentences.length)];
    File file = new File("resource/c_stat");
    //VClientInfo info = null;
    Long time =0L;
    String id = "";
	String ip = "";
	String port = "";
	int bandwidth = 0;
	int buffer_size = 0;
	int framerate = 0;
	

    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        String line;
        
		while ((line = br.readLine()) != null) {
			   // process the line.
				String[] parts = line.split(":");
				id = parts[0];
				ip = parts[1];
				//String port = parts[2];
				buffer_size = Integer.valueOf(parts[2]);
				bandwidth = Integer.valueOf(parts[3]);
				framerate = Integer.valueOf(parts[4]);
				time = System.currentTimeMillis();
				//info = new VClientInfo(id, ip, buffer_size, bandwidth, framerate);
		}
    } catch (FileNotFoundException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}

    
    //_collector.emit(new Values(time, info));
    _collector.emit(new Values(time, id, ip, port, bandwidth, buffer_size, framerate));
  }

  @Override
  public void ack(Object id) {
  }

  @Override
  public void fail(Object id) {
  }

  @Override
  public void declareOutputFields(OutputFieldsDeclarer declarer) {
    declarer.declare(new Fields("time", "id", "clientstat"));
  }

}
