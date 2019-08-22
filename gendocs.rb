#!/usr/bin/env ruby

require 'asciidoctor'

docFiles = {
    "javatest/javatest-junit/docs/README.adoc" => "javatest/javatest-junit/README.adoc"
}

docFiles.each {
    |source, destination|
    puts "Coalescing #{source} into #{destination}"

    doc = Asciidoctor.load_file source, safe: :unsafe, header_only: true

    # NOTE quick and dirty way to get the attributes set or unset by the document header
    header_attr_names = (doc.instance_variable_get :@attributes_modified).to_a
    header_attr_names.each {|k| doc.attributes[%(#{k}!)] = '' unless doc.attr? k }

    doc = Asciidoctor.load_file source, safe: :unsafe, parse: false

    lines = doc.reader.read.gsub(/^include::(?=.*\[\]$)/m, '\\include::')

    File.open(destination, 'w') {|f| f.write lines }
}

