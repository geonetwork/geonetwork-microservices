<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:gn-util="https://geonetwork-opensource.org/gn-util"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                version="3.0">

  <xsl:output method="html"
              media-type="text/html"
              encoding="UTF-8"
              indent="no"/>

  <xsl:import href="classpath:xslt/core/commons/xsl-params-core.xsl"/>
  <xsl:import href="classpath:xslt/core/themes/default/theme.xsl"/>
  <xsl:import href="collection-util.xsl"/>

  <xsl:template match="/">
    <xsl:message><xsl:copy-of select="."/></xsl:message>

    <xsl:variable name="collection"
                  select="list/values"
                  as="node()?"/>

    <xsl:variable name="label"
                  select="gn-util:getCollectionName($collection, $language)"
                  as="xs:string"/>

    <xsl:variable name="properties"
                  select="tokenize($label, '\|')"
                  as="xs:string*"/>

    <xsl:variable name="title"
                    select="if (empty($properties[1])) then name else $properties[1]"/>
    <xsl:variable name="subTitle"
                    select="if (empty($properties[2])) then '' else $properties[2]"/>

    <html>
      <xsl:attribute name="lang" select="$language"/>
      <xsl:call-template name="html-head">
        <xsl:with-param name="title" select="$title"/>
      </xsl:call-template>
      <xsl:call-template name="html-body">
        <xsl:with-param name="title" select="$title"/>
        <xsl:with-param name="content">
          <xsl:call-template name="html-breadcrumb">
            <xsl:with-param name="breadcrumb" select="$subTitle"/>
          </xsl:call-template>
        </xsl:with-param>
      </xsl:call-template>
    </html>
  </xsl:template>
</xsl:stylesheet>