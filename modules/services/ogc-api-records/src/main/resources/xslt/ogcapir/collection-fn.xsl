<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:gn-ogcapir-util="https://geonetwork-opensource.org/gn-ogcapir-util"
                exclude-result-prefixes="#all"
                version="3.0">

  <xsl:function name="gn-ogcapir-util:getCollectionName" as="xs:string">
    <xsl:param name="collection" as="element(source)"/>
    <xsl:param name="language" as="xs:string"/>

    <xsl:value-of select="$collection/(
                            labelTranslations/entry[key = $language3letters]/value[. != '']
                            |name[. != '']
                            |uuid)[1]"/>
  </xsl:function>


  <!-- Collection logo is:
   * in logo folder for main portal and harvester.
   * in harvester folder for sub portal.
  -->
  <xsl:function name="gn-ogcapir-util:getCollectionLogo" as="xs:string">
    <xsl:param name="collection" as="element(source)"/>

    <xsl:value-of select="if ($collection/type = ('harvester', 'portal'))
                          then concat($geonetworkUrl, '/', $logoFolder, '/', $collection/uuid, '.png')
                          else concat($geonetworkUrl, '/', $harvestingFolder, '/', $collection/logo)"/>
  </xsl:function>
</xsl:stylesheet>