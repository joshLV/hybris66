
hybris Changes
=============================

This is a modified version of Solr.

The following directories were removed:
- docs
- example

The following files/directories were added:
- HYBRIS_README.txt
- contrib/hybris
- server/solr/security.json.example
- server/solr/solr.jks

The following files/directories were modified/replaced:
- bin/solr.cmd:
	- workaround for https://issues.apache.org/jira/browse/SOLR-7283 (lines 19-20)
- bin/solr.in.cmd:
	- authentication and ssl configuration example (lines 133-146)
- bin/solr.in.sh:
	- authentication and ssl configuration example (lines 150-163)
- server/solr/solr.xml
- server/solr/configsets

