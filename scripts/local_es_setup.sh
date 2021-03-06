#!/bin/bash

curl -XPUT ${1}:9200/_template/template_foxtrot_mappings -d '
{
    "template" : "foxtrot-*",
    "settings" : {
        "number_of_shards" : 10,
        "number_of_replicas" : 1
    },
    "mappings" : {
        "document" : {
            "_source" : { "enabled" : false },
            "_all" : { "enabled" : false },
            "_timestamp" : { "enabled" : true },

            "dynamic_templates" : [
                {
                    "template_timestamp" : {
                        "match" : "timestamp",
                        "mapping" : {
                            "store" : false,
                            "index" : "not_analyzed",
                            "type" : "date"
                        }
                    }
                },
                {
                    "template_no_store_analyzed" : {
                        "match" : "*",
                        "match_mapping_type" : "string",
                        "mapping" : {
                            "store" : false,
                            "index" : "not_analyzed",
                            "fields" : {
                                "analyzed": {
                                    "store" : false,
                                    "type": "string",
                                    "index": "analyzed"
                                }
                            }
                        }
                    }
                },
                {
                    "template_no_store" : {
                        "match_mapping_type": "date|boolean|double|long|integer",
                        "match_pattern": "regex",
                        "path_match": ".*",
                        "mapping" : {
                            "store" : false,
                            "index" : "not_analyzed"
                        }
                    }
                }
            ]
        }
    }
}'

#curl -XPUT "http://${1}:9200/consoles/" -d '{
#    "settings" : {
#        "index" : {
#            "number_of_shards" : 1,
#            "number_of_replicas" : 2
#        }
#    }
#}'
#
#curl -XPUT "http://${1}:9200/table-meta/" -d '{
#    "settings" : {
#        "index" : {
#            "number_of_shards" : 1,
#            "number_of_replicas" : 2
#        }
#    }
#}'
