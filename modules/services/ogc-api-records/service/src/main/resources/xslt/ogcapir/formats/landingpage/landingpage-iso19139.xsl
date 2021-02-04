<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:gmd="http://www.isotc211.org/2005/gmd"
                xmlns:gco="http://www.isotc211.org/2005/gco"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                exclude-result-prefixes="#all"
                version="3.0">

  <xsl:include href="../../schemas/iso19139-tpl.xsl"/>

  <xsl:template match="gmd:MD_Metadata"
                mode="landingpage">
    <div class="flex space-x-4 mb-4 w-full">
      <section class="rounded shadow border border-gray-200 bg-white w-1/3">
        <div class="px-3 py-4 sm:px-5 bg-gray-50">
          <h3 class="font-medium">Data</h3>
        </div>

        <div class="border-t border-gray-200 flex flex-wrap p-4">
          <gn-ui-record-data uuid="12345" bbox="" links="">
            <img src="{$geonetworkUrl}/srv/api/records/{gmd:fileIdentifier/*/text()}/extents.png"
                class="w-full"/>
          </gn-ui-record-data>
        </div>
      </section>

      <section class="rounded shadow border border-gray-200 bg-white w-2/3">
        <div class="px-3 py-4 sm:px-5 bg-gray-50">
          <h3 class="font-medium">Summary</h3>
        </div>

        <div class="border-t border-gray-200 flex flex-wrap">
          <xsl:apply-templates mode="landingpage"
                                select="gmd:identificationInfo/*/gmd:citation/*/gmd:edition
                                      |gmd:identificationInfo/*/gmd:citation/*/gmd:date
                                      |gmd:identificationInfo/*/gmd:resourceMaintenance"/>
        </div>
      </section>

    </div>

    <section class="w-full rounded shadow border border-gray-200 bg-white mb-4">
      <div class="px-3 py-4 sm:px-5 bg-gray-50">
        <h3 class="font-medium">Informations</h3>
      </div>
      <div class="border-t border-gray-200 flex flex-wrap">
        <xsl:apply-templates mode="landingpage" select="gmd:identificationInfo"/>
      </div>
    </section>

    <section class="w-full rounded shadow border border-gray-200 bg-white mb-4">
      <div class="px-3 py-4 sm:px-5 bg-gray-50">
        <h3 class="font-medium">Download</h3>
      </div>
      <div class="border-t border-gray-200 flex flex-wrap">
        <xsl:apply-templates mode="landingpage" select="gmd:distributionInfo"/>
      </div>
    </section>

    <section class="w-full rounded shadow border border-gray-200 bg-white mb-4">
      <div class="px-3 py-4 sm:px-5 bg-gray-50">
        <h3 class="font-medium">More info</h3>
      </div>
      <div class="border-t border-gray-200 flex flex-wrap">
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
    <xsl:call-template name="render-field">
      <xsl:with-param name="label" select="if ($label) then $label else $labelKey"/>
      <xsl:with-param name="text">
        <xsl:call-template name="getText-iso19139"/>
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
    <xsl:call-template name="render-field">
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
    <xsl:call-template name="render-field">
      <xsl:with-param name="label" select="$label"/>
      <xsl:with-param name="text">
        <xsl:value-of select="@codeListValue"/>
      </xsl:with-param>
    </xsl:call-template>
  </xsl:template>
</xsl:stylesheet>