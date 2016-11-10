import { Pipe, PipeTransform } from '@angular/core';
import * as marked from 'marked';

@Pipe({
  name: 'markdownToHtml'
})
export class MarkdownToHtmlPipe implements PipeTransform {

  transform(markdown: any, options?: any): any {
    if (markdown == null) {
      return '';
    }

    return marked(markdown, options);
  }

}
