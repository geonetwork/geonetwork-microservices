<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:gmd="http://www.isotc211.org/2005/gmd"
                xmlns:gco="http://www.isotc211.org/2005/gco"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                version="3.0">
  <xsl:output method="html"
              media-type="text/html"
              encoding="UTF-8"
              indent="no"/>

  <xsl:import href="commons/xsl-params-core.xsl"/>
  <xsl:import href="themes/default/theme.xsl"/>

  <xsl:template match="/">
    <html>
      <xsl:attribute name="lang" select="$language"/>
      <xsl:call-template name="html-head"/>
      <xsl:call-template name="html-body">
        <xsl:with-param name="content">

          <xsl:call-template name="html-breadcrumb">
            <xsl:with-param name="breadcrumb">
              <p class="uppercase tracking-loose w-full">Looking for data?</p>
              <h1 class="my-4 text-5xl font-bold leading-tight">
                Search over 12534 data sets, services and maps, ...
              </h1>
              <p class="leading-normal text-2xl mb-8">
                ... <xsl:value-of select="/source/@id"/>
              </p>
              <button class="mx-auto lg:mx-0 hover:underline bg-white text-gray-800 font-bold rounded-full my-6 py-4 px-8 shadow-lg focus:outline-none focus:shadow-outline transform transition hover:scale-105 duration-300 ease-in-out">
                Browse ...
              </button>
            </xsl:with-param>
          </xsl:call-template>

          <section class="border-b py-8">
            <div class="container mx-auto flex flex-wrap pt-4 pb-12">
              <h1 class="w-full my-2 text-5xl font-bold leading-tight text-center text-gray-800">
                Thematic portals
              </h1>
              <div class="w-full mb-4">
                <div class="h-1 mx-auto gradient w-64 opacity-25 my-0 py-0 rounded-t"></div>
              </div>
              <div class="w-full md:w-1/3 p-6 flex flex-col flex-grow flex-shrink">
                <div class="flex-1 bg-white rounded-t rounded-b-none overflow-hidden shadow">
                  <a href="#" class="flex flex-wrap no-underline hover:no-underline">
                    <p class="w-full text-gray-600 text-xs md:text-sm px-6">
                      xGETTING STARTED aaa
                    </p>
                    <div class="w-full font-bold text-xl text-gray-800 px-6">
                      Biodiversity
                    </div>
                    <p class="text-gray-800 text-base px-6 mb-5">
                      Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam at ipsum eu nunc commodo posuere et sit amet ligula.
                    </p>
                  </a>
                </div>
                <div class="flex-none mt-auto bg-white rounded-b rounded-t-none overflow-hidden shadow p-6">
                  <div class="flex items-center justify-start">
                    <button class="mx-auto lg:mx-0 hover:underline gradient text-white font-bold rounded-full my-6 py-4 px-8 shadow-lg focus:outline-none focus:shadow-outline transform transition hover:scale-105 duration-300 ease-in-out">
                      More ...
                    </button>
                  </div>
                </div>
              </div>
              <div class="w-full md:w-1/3 p-6 flex flex-col flex-grow flex-shrink">
                <div class="flex-1 bg-white rounded-t rounded-b-none overflow-hidden shadow">
                  <a href="#" class="flex flex-wrap no-underline hover:no-underline">
                    <p class="w-full text-gray-600 text-xs md:text-sm px-6">
                      xGETTING STARTED
                    </p>
                    <div class="w-full font-bold text-xl text-gray-800 px-6">
                      Climate change
                    </div>
                    <p class="text-gray-800 text-base px-6 mb-5">
                      Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam at ipsum eu nunc commodo posuere et sit amet ligula.
                    </p>
                  </a>
                </div>
                <div class="flex-none mt-auto bg-white rounded-b rounded-t-none overflow-hidden shadow p-6">
                  <div class="flex items-center justify-center">
                    <button class="mx-auto lg:mx-0 hover:underline gradient text-white font-bold rounded-full my-6 py-4 px-8 shadow-lg focus:outline-none focus:shadow-outline transform transition hover:scale-105 duration-300 ease-in-out">
                      More ...
                    </button>
                  </div>
                </div>
              </div>
              <div class="w-full md:w-1/3 p-6 flex flex-col flex-grow flex-shrink">
                <div class="flex-1 bg-white rounded-t rounded-b-none overflow-hidden shadow">
                  <a href="#" class="flex flex-wrap no-underline hover:no-underline">
                    <p class="w-full text-gray-600 text-xs md:text-sm px-6">
                      xGETTING STARTED
                    </p>
                    <div class="w-full font-bold text-xl text-gray-800 px-6">
                      Water
                    </div>
                    <p class="text-gray-800 text-base px-6 mb-5">
                      Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam at ipsum eu nunc commodo posuere et sit amet ligula.
                    </p>
                  </a>
                </div>
                <div class="flex-none mt-auto bg-white rounded-b rounded-t-none overflow-hidden shadow p-6">
                  <div class="flex items-center justify-end">
                    <button class="mx-auto lg:mx-0 hover:underline gradient text-white font-bold rounded-full my-6 py-4 px-8 shadow-lg focus:outline-none focus:shadow-outline transform transition hover:scale-105 duration-300 ease-in-out">
                      More ...
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </section>


        </xsl:with-param>
      </xsl:call-template>
    </html>
  </xsl:template>
</xsl:stylesheet>