#!/usr/bin/env ruby

require 'asciidoctor'

docFiles = {
    "javatest/javatest-junit/docs/README.adoc" => "javatest/javatest-junit/README.adoc",
    "javatest/javatest-eventually/docs/README.adoc" => "javatest/javatest-eventually/README.adoc",
    "javatest/javatest-fixtures/docs/README.adoc" => "javatest/javatest-fixtures/README.adoc"
}

docFiles.each {
    |source, destination|
    puts "Coalescing #{source} into #{destination}"

    doc = Asciidoctor.load_file source, safe: :unsafe, parse: false

    lines = doc.reader.read.gsub(/^include::(?=.*\[\]$)/m, '\\include::')

    File.open(destination, 'w') {|f| f.write lines }
}

