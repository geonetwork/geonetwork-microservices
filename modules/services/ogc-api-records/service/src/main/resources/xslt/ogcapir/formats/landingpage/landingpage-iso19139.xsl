<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:gmd="http://www.isotc211.org/2005/gmd"
                xmlns:gco="http://www.isotc211.org/2005/gco"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                exclude-result-prefixes="#all"
                version="3.0">

  <xsl:import href="../../schemas-utils/util-iso19139.xsl"/>
  <xsl:import href="../../html-util.xsl"/>

  <xsl:template match="gmd:MD_Metadata"
                mode="landingpage">
    <div class="flex space-x-2 mb-4">
      <section class="rounded border-2 bg-white p-2 w-2/3">
        <div class="pb-2 border-solid border-b-4">
          <h1 class="text-4xl text-blue-900 font-medium">Data</h1>
        </div>

        <gn-ui-record-data uuid="12345" bbox="" links="">
          <img src="{$geonetworkUrl}/srv/api/records/{gmd:fileIdentifier/*/text()}/extents.png"
               class="w-full rounded"/>
        </gn-ui-record-data>
      </section>

      <section class="rounded border-2 bg-white p-2 w-1/3">
        <div class="pb-2 border-solid border-b-4">
          <h1 class="text-4xl text-blue-900 font-medium">Summary</h1>
        </div>

        <div class="flex flex-wrap">
          <xsl:apply-templates mode="landingpage"
                               select="gmd:identificationInfo/*/gmd:citation/*/gmd:edition
                                      |gmd:identificationInfo/*/gmd:citation/*/gmd:date
                                      |gmd:identificationInfo/*/gmd:resourceMaintenance"/>
        </div>
      </section>

    </div>

    <section class="w-full rounded border-2 bg-white p-2">
      <div class="pb-2 border-solid border-b-4">
        <h1 class="text-4xl text-blue-900 font-medium">Informations</h1>

        <div class="flex flex-wrap">
          <xsl:apply-templates mode="landingpage" select="gmd:identificationInfo"/>
        </div>
      </div>
    </section>

    <section class="w-full rounded border-2 bg-white p-2">
      <div class="pb-2 border-solid border-b-4">
        <h1 class="text-4xl text-blue-900 font-medium">Download</h1>
      </div>

      <div class="flex flex-wrap">
        <xsl:apply-templates mode="landingpage" select="gmd:distributionInfo"/>
      </div>
    </section>

    <section class="w-full rounded border-2 bg-white p-2">
      <div class="pb-2 border-solid border-b-4">
        <h1 class="text-4xl text-blue-900 font-medium">More info</h1>
      </div>

      <div class="flex flex-wrap">
        <xsl:apply-templates mode="landingpage"
                             select="*[
                             name() != 'gmd:identificationInfo'
                             and name() != 'gmd:distributionInfo']"/>
      </div>
    </section>



  </xsl:template>


  <!-- Fields rendered elsewhere -->
  <xsl:template match="gmd:identificationInfo/*/gmd:citation/*/gmd:title
                      |gmd:abstract
                      |gmd:graphicOverview"
                mode="landingpage" priority="2"/>


  <xsl:template match="gmd:*[gco:CharacterString]"
                mode="landingpage">

    <xsl:variable name="labelKey"
                  select="name(.)"
                  as="xs:string"/>
    <xsl:variable name="label"
                  select="($i18nStandard/labels/element[@name = $labelKey]/label/text())[1]"/>
    <xsl:call-template name="html-field">
      <xsl:with-param name="label" select="$label"/>
      <xsl:with-param name="text">
        <xsl:call-template name="getText"/>
      </xsl:with-param>
    </xsl:call-template>
  </xsl:template>


  <xsl:template match="gmd:date"
                mode="landingpage">

    <!--<xsl:variable name="labelKey"
                  select="name(.)"
                  as="xs:string"/>
    <xsl:variable name="label"
                  select="($i18nStandard/labels/element[@name = $labelKey]/label/text())[1]"/>-->
    <xsl:call-template name="html-field">
      <xsl:with-param name="label" select="*/gmd:dateType/*/@codeListValue"/>
      <xsl:with-param name="text">
        <xsl:value-of select="*/gmd:date/*/text()"/>
      </xsl:with-param>
    </xsl:call-template>
  </xsl:template>

  
  <xsl:template match="gmd:*[@codeListValue]"
                mode="landingpage">

    <xsl:variable name="labelKey"
                  select="name(.)"
                  as="xs:string"/>
    <xsl:variable name="label"
                  select="($i18nStandard/labels/element[@name = $labelKey]/label/text())[1]"/>
    <xsl:call-template name="html-field">
      <xsl:with-param name="label" select="$label"/>
      <xsl:with-param name="text">
        <xsl:value-of select="@codeListValue"/>
      </xsl:with-param>
    </xsl:call-template>
  </xsl:template>
</xsl:stylesheet>