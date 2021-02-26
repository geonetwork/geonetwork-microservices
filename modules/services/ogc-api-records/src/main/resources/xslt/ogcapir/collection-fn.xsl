<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:gn-ogcapir-util="https://geonetwork-opensource.org/gn-ogcapir-util"
                exclude-result-prefixes="#all"
                version="3.0">

  <xsl:function name="gn-ogcapir-util:getCollectionName" as="xs:string">
    <xsl:param name="collection" as="element(collection)"/>

    <xsl:value-of select="$collection/(
                            title[. != '']
                            |name[. != '']
                            |uuid)[1]"/>
  </xsl:function>

  <xsl:function name="gn-ogcapir-util:getCollectionDescription" as="xs:string">
    <xsl:param name="collection" as="element(collection)"/>

    <xsl:value-of select="$collection/description[. != '']"/>
  </xsl:function>

  <!-- Collection logo is:
   * in logo folder for main portal and harvester.
   * in harvester folder for sub portal.
  -->
  <xsl:function name="gn-ogcapir-util:getCollectionLogo" as="xs:string">
    <xsl:param name="collection" as="element(collection)"/>

    <xsl:value-of select="if ($collection/source/type = ('harvester', 'portal'))
                          then concat($geonetworkUrl, '/', $logoFolder, '/', $collection/source/uuid, '.png')
                          else concat($geonetworkUrl, '/', $harvestingFolder, '/', $collection/source/logo)"/>
  </xsl:function>
</xsl:stylesheet>