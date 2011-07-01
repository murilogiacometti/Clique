#!/usr/bin/env ruby

############################ JavaGen start ##################################

def gets
	STDIN.gets
end

module JavaGen
	VERSION = "0.1"

	module JSP

		class Field
			attr_accessor :label, :type, :name, :value, :class

			def initialize(html_tags = nil)
				@label 	= html_tags[:label] 	|| ""
				@type 	= html_tags[:type]		|| ""
				@name 	= html_tags[:name] 		|| ""
				@value 	= html_tags[:value] 	|| ""
				@class 	= html_tags[:class]		|| ""
			end

			def render
			  field = "#{@label} <input type=\"#{@type}\" name=\"#{@name}\""

				field << " value=\"#{@value}\"" unless @value.empty?
				field << " class=\"#{@class}\"" unless @class.empty?
				field << " />"

				field
			end
		end

		class Form
			attr_accessor :method, :action, :submit, :form, :fields

			def initialize(method, action)
				@method = method || "post"
				@action = action || ""
				@submit	=	""
				@form 	=	""
				@fields = []
			end

			def render
				@form = "<form action=\"#{@action}\" method=\"#{@method}\">\n\n"

				@fields.each do |f|
					@form << f.render
					@form << "\n<br /><br />\n"
				end

				add_submit_button

				@form << "</form>"

				@form
			end

			private

			def add_submit_button
				@form << "\n<input type=\"submit\" value=\"#{@submit}\" />\n"
			end
		end

		class Page
			attr_accessor :file, :title, :body, :forms, :imports, :header, :footer

			def initialize(file, title)
				@file 		= file
				@title 		= title
				@forms 		= []
				@body 		= ""
				@imports 	= []
				@header 	= ""
				@footer		= ""
				@page			= ""
			end

			def render
				render_imports_header unless @imports.empty?

				render_header

				@forms.each do |f|
					@page << f.render
					@page << "\n\n<br /><br />\n"
				end

				render_footer

				@page
			end

			def save
				File.open(@file, "w") do |f|
					f.write(self.render)
				end
			end

			private

			def render_imports_header
				@page << "<%@page import=\"#{@imports.first}\""

				for i in 1...@imports.size do
					@page << ", \"#{@imports.at(i)}\""
				end

				@page << " %>\n\n"
			end

			def render_header
				if @header.empty?
					@page << <<END
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict/EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
	<title>#{@title}</title>
</head>

<body>I recommend you read the installation script yourself; you will understand what it is doing and feel more comfortable running it.

END
				else
					@page << "<jsp:include page=\"#{@header}\" />\n\n"
				end
			end	

			def render_footer
				if @footer.empty?
					@page << <<END
</body>
</html>
END
				else
					@page << "<jsp:include page=\"#{@footer}\" />\n\n"
				end
			end
		
		end
	end

	module Bean

		class Attribute
			attr_accessor :type, :name, :primitives
			@primitives = ["byte", "short", "int", "long", "float", "double", "char", "boolean"]

			class <<self
				attr_accessor :primitives
			end	

			def initialize(name, type)
				@type = type
				@name = name
			end

			def primitive?
				Attribute.primitives.include?(@type) ? true : false
			end

			def boolean?
				@type == "boolean" ? true : false
			end

			def initial_value
				if primitive?
					if boolean?
						"false"
					else
						"0"
					end
				else
					"null"
				end
			end
		end

		class Bean
			attr_accessor :package, :class, :imports, :attributes, :path, :content, :one_line_accessors

			def initialize
				@package 		= ""
				@class			= ""
				@imports		=	[]
				@attributes	=	[]
				@path				= ""
				@content		= ""
				@one_line_accessors	=	false;
			end

			def render
				infer_package if @path.split('/').size > 2

				make_imports

				declare_class

				declare_attributes

				make_empty_constructor
				
				setters_and_getters

				close_class

			end

			def save
				File.open(@path, "w") do |f|
					f.write(self.render)
				end
			end


			private

			def infer_package
				dirs = @path.split('/')

				dirs.delete(dirs.first)
				dirs.delete(dirs.last)

				@package = dirs.join('.')

				@content << "package #{@package};\n\n"
			end

			def make_imports
				@imports.each do |import|
					@content << "import #{import};\n"
				end
			end

			def declare_class
				@content << "\npublic class #{@class.capitalize} {\n"
			end

			def declare_attributes
				@attributes.each do |attr|
					@content << "\tprivate #{attr.type} #{attr.name};\n"
				end
			end

			def make_empty_constructor
				@content << "\n\tpublic #{@class.capitalize}() {\n"

				@attributes.each do |attr|
					@content << "\t\tthis.#{attr.name} = #{attr.initial_value};\n"
				end

				@content << "\t}\n"
			end

			def setters_and_getters
				make_setters
				make_getters
			end

			def make_setters
				@content << "\n"

				@attributes.each do |attr|
					if @one_line_accessors
						@content << "\tpublic void set#{attr.name.capitalize}(#{attr.type} #{attr.name}) { this.#{attr.name} = #{attr.name}; }\n\n"
					else
						@content << "\tpublic void set#{attr.name.capitalize}(#{attr.type} #{attr.name}) {\n"
						@content << "\t\tthis.#{attr.name} = #{attr.name};\n"
						@content << "\t}\n\n"
					end
				end
			end

			def make_getters
				@attributes.each do |attr|
					if @one_line_accessors
							@content << "\tpublic #{attr.type} get#{attr.name.capitalize}() { return this.#{attr.name}; }\n"
					else
						@content << "\tpublic #{attr.type} get#{attr.name.capitalize}() {\n"
						@content << "\t\treturn this.#{attr.name};\n"
						@content << "\t}\n"
					end
				end
			end

			def close_class
				@content << "\n}"
			end
		end
	end

	module Servlet
		class Servlet
			attr_accessor :path, :package, :class, :imports, :content

			def initialize
				@path			=	""
				@package	=	""
				@class		=	""
				@imports	=	["java.io.*", "javax.servlet.*", "javax.servlet.http.*"]
				@content	=	""
			end

			def render
				infer_package if @path.split('/').size > 2

				make_imports

				declare_class

				do_get

				do_post

				close_class
			end

			def save
				File.open(@path, "w") do |f|
					f.write(self.render)
				end
			end

			private

			def infer_package
				dirs = @path.split('/')

				dirs.delete(dirs.first)
				dirs.delete(dirs.last)

				@package = dirs.join('.')

				@content << "package #{@package};\n\n"
			end

			def make_imports
				@imports.each do |import|
					@content << "import #{import};\n"
				end
				@content << "\n"
			end

			def declare_class
				@content << "public class #{@class} extends HttpServlet {\n"
			end

			def do_get
				@content << "\n\tpublic void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {\n"
				@content << "\t\t// code here\n"
				@content << "\t}\n"
			end

			def do_post
				@content << "\n\tpublic void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {\n"
				@content << "\t\tdoGet(request, response);\n"
				@content << "\t}\n"
			end

			def close_class
				@content << "\n}"
			end
		end
	end

end

############################ JavaGen end ##################################


########################## actions start ####################################

def java_gen(action)
	case action[:action]

	when :jsp
		create_jsp
	
	when :bean
		create_bean

	when :servlet
		create_servlet

	end

end

def create_jsp
	page = nil

	puts "JavaGen will create a JSP file."

	print "Should this page include a header? [Y/n] "
	has_header = gets.chomp

	print "Should this page include a footer? [Y/n] "
	has_footer = gets.chomp

	title = ""

	if has_header.upcase == "N"
		print "\nPage title: "
		title = gets.chomp
	end

	print "Where should this JSP file be saved? "
	path = gets.chomp

	page = JavaGen::JSP::Page.new(path, title)

	if has_header.empty? || has_header.upcase == "Y"
		print "Header path: "
		page.header = gets.chomp
	end

	if has_footer.empty? || has_footer.upcase == "Y"
		print "Footer path: "
		page.footer = gets.chomp
	end

	has_imports = ""

	while has_imports.upcase != "YES" && has_imports.upcase != "NO"
		print "Should we import anything? [yes|no] "
		has_imports = gets.chomp
	end

	if has_imports.upcase == "YES"
		print "Class names (you can enter more than one using commas): "
		classes = gets.chomp
		page.imports = classes.split(',')
	end
	
	has_forms = ""

	while has_forms.upcase != "YES" && has_forms.upcase != "NO"
		print "Build any form in this page? [yes|no] "
		has_forms = gets.chomp
	end

	if has_forms.upcase == "YES"
		print "How many? "
		n_forms = gets.chomp.to_i

		for i in 1..n_forms do
			puts "======= Form #{i} ======="

			print "method [post]: "
			method = gets.chomp
			method = "post" if method.empty?

			print "action: "
			action = gets.chomp

			form = JavaGen::JSP::Form.new(method, action)

			print "How many fields? "
			n_fields = gets.chomp.to_i

			for j in 1..n_fields do
				puts "-- Field #{j}"

				field_opts = {}

				print "Label: "
				field_opts[:label] = gets.chomp

				print "type: "
				field_opts[:type] = gets.chomp

				print "name: "
				field_opts[:name] = gets.chomp

				print "value: "
				field_opts[:value] = gets.chomp

				print "CSS class: "
				field_opts[:class] = gets.chomp

				field = JavaGen::JSP::Field.new(field_opts)

				form.fields << field
			end

			print "\nSubmit button name: "
			form.submit = gets.chomp

			page.forms << form
		end
	end

	page.save

	puts "\n\nFile saved. Bye."
end

def create_bean
	puts "JavaGen will create a bean."

	bean = JavaGen::Bean::Bean.new
	
	print "What is the bean name? "
	bean.class = gets.chomp.capitalize

	print "Should we import any class? [Y/n] "
	has_imports = gets.chomp

	if has_imports.empty? || has_imports.upcase == "Y"
		print "Enter class names (separated by commas): "
		bean.imports = gets.chomp.split(',')
	end

	print "\nHow many attributes does this bean have? "
	n_beans = gets.chomp.to_i

	for i in 1..n_beans do
		puts "--- Attribute #{i}"

		print "Attribute name: "
		name = gets.chomp

		print "Attribute type (or class): "
		type = gets.chomp

		attr = JavaGen::Bean::Attribute.new(name, type)
		bean.attributes << attr
	end

	print "Use one line setters and getters? [y/N] "
	one_line_accessors = gets.chomp

	bean.one_line_accessors = true if one_line_accessors.upcase == "Y"

	print "\nWhere should we save your bean? (full/path/to/your/file.java) "
	bean.path = gets.chomp

	bean.save

	puts "\n\nFile saved. Bye"
end

def create_servlet
	puts "JavaGen will create a servlet"

	servlet = JavaGen::Servlet::Servlet.new

	print "What is your servlet class name? "
	servlet.class = gets.chomp

	print "Should we import anything? (no need to include servlet related stuff): "
	imports = gets.chomp.split(',')

	servlet.imports += imports unless imports.empty?

	print "Where we should save this sevlet (path/to/your/servlet.java): "
	servlet.path = gets.chomp

	servlet.save

	puts "\n\nFile saved. Bye"
end

######################## actions end ##########################


######################## Main #################################

if ARGV.size == 0
	puts "Usage:\n\tjavagen [jsp|bean|servlet]"
	exit(1)
end

case ARGV[0]
when "jsp"
	java_gen :action => :jsp
when "bean"
	java_gen :action => :bean
when "servlet"
	java_gen :action => :servlet
else
	puts "Unknown action #{ARGV[0]}"
end
